// DOM元素
const loginTab = document.getElementById('login-tab');
const registerTab = document.getElementById('register-tab');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const userLoginForm = document.getElementById('user-login-form');
const userRegisterForm = document.getElementById('user-register-form');

// 初始化
document.addEventListener('DOMContentLoaded', function() {
    // 绑定事件
    if (loginTab) loginTab.addEventListener('click', showLoginForm);
    if (registerTab) registerTab.addEventListener('click', showRegisterForm);
    if (userLoginForm) userLoginForm.addEventListener('submit', handleUserLogin);
    if (userRegisterForm) userRegisterForm.addEventListener('submit', handleUserRegister);
});

// 显示登录表单
function showLoginForm() {
    loginTab.classList.add('active');
    registerTab.classList.remove('active');
    loginForm.classList.add('active');
    registerForm.classList.remove('active');
}

// 显示注册表单
function showRegisterForm() {
    loginTab.classList.remove('active');
    registerTab.classList.add('active');
    loginForm.classList.remove('active');
    registerForm.classList.add('active');
}

// 处理用户登录
async function handleUserLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    
    if (!username || !password) {
        showMessage('请输入用户名和密码', 'error');
        return;
    }
    
    try {
        const response = await authAPI.login(username, password);
        
        if (response.code === 200) {
            // 登录成功
            const { token, user } = response.data;
            
            // 保存token和用户信息
            setToken(token);
            setUserInfo(user);
            
            showMessage('登录成功，正在跳转...', 'success');
            
            // 延迟跳转，让用户看到成功消息
            setTimeout(() => {
                // 根据当前页面位置确定正确的跳转路径
                if (window.location.pathname.includes('/pages/')) {
                    // 如果当前在pages目录下，需要返回上一级
                    window.location.href = 'user-dashboard.html';
                } else {
                    // 如果在根目录，使用相对路径
                    window.location.href = 'pages/user-dashboard.html';
                }
            }, 1000);
        } else {
            // 登录失败
            showMessage(response.message || '登录失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '登录失败，请稍后重试', 'error');
    }
}

// 处理用户注册
async function handleUserRegister(event) {
    event.preventDefault();
    
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;
    const email = document.getElementById('register-email').value;
    
    if (!username || !password || !email) {
        showMessage('请填写所有必填字段', 'error');
        return;
    }
    
    if (password.length < 6) {
        showMessage('密码长度至少为6位', 'error');
        return;
    }
    
    try {
        const response = await authAPI.register({
            username,
            password,
            email,
            role: 'USER'
        });
        
        if (response.code === 200) {
            // 注册成功
            showMessage('注册成功，请登录', 'success');
            
            // 清空表单
            userRegisterForm.reset();
            
            // 切换到登录表单
            setTimeout(() => {
                showLoginForm();
            }, 1500);
        } else {
            // 注册失败
            showMessage(response.message || '注册失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '注册失败，请稍后重试', 'error');
    }
}

// 显示消息提示
function showMessage(message, type = 'info') {
    const messageContainer = document.getElementById('message-container');
    
    // 创建消息元素
    const messageElement = document.createElement('div');
    messageElement.className = `message ${type}`;
    messageElement.textContent = message;
    
    // 添加到容器
    messageContainer.appendChild(messageElement);
    
    // 3秒后自动移除
    setTimeout(() => {
        if (messageElement.parentNode) {
            messageElement.parentNode.removeChild(messageElement);
        }
    }, 3000);
}

// 检查用户是否已登录
function isLoggedIn() {
    return !!getToken();
}

// 检查管理员是否已登录
function isAdminLoggedIn() {
    const userInfo = getUserInfo();
    return userInfo && userInfo.role === 'ADMIN';
}

// 登出
function logout() {
    clearToken();
    clearUserInfo();
    window.location.href = '../index.html';
}

// 格式化日期
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// 格式化图书状态
function formatBookStatus(status) {
    return status === 'AVAILABLE' ? '可借阅' : '已借出';
}

// 格式化借阅状态
function formatBorrowStatus(status) {
    switch (status) {
        case 'BORROWED': return '借阅中';
        case 'RETURNED': return '已归还';
        case 'OVERDUE': return '已逾期';
        default: return status;
    }
}