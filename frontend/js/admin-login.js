// DOM元素
const adminLoginForm = document.getElementById('admin-login-form');

// 初始化
document.addEventListener('DOMContentLoaded', function() {
    // 绑定事件
    adminLoginForm.addEventListener('submit', handleAdminLogin);
});

// 处理管理员登录
async function handleAdminLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('admin-username').value;
    const password = document.getElementById('admin-password').value;
    
    if (!username || !password) {
        showMessage('请输入用户名和密码', 'error');
        return;
    }
    
    try {
        const response = await authAPI.adminLogin(username, password);
        
        if (response.code === 200) {
            // 登录成功
            const { token, admin } = response.data;
            
            // 保存token和管理员信息
            setToken(token);
            setUserInfo(admin);
            
            showMessage('登录成功，正在跳转...', 'success');
            
            // 延迟跳转，让用户看到成功消息
            setTimeout(() => {
                // 根据当前页面位置确定正确的跳转路径
                if (window.location.pathname.includes('/pages/')) {
                    // 如果当前在pages目录下，需要返回上一级
                    window.location.href = 'admin-dashboard.html';
                } else {
                    // 如果在根目录，使用相对路径
                    window.location.href = 'pages/admin-dashboard.html';
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