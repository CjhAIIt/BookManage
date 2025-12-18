// API配置
const API_BASE_URL = 'http://localhost:8082/api';

// 获取token
function getToken() {
    return localStorage.getItem('token');
}

// 设置token
function setToken(token) {
    localStorage.setItem('token', token);
}

// 清除token
function clearToken() {
    localStorage.removeItem('token');
}

// 获取用户信息
function getUserInfo() {
    const userInfo = localStorage.getItem('userInfo');
    return userInfo ? JSON.parse(userInfo) : null;
}

// 设置用户信息
function setUserInfo(userInfo) {
    localStorage.setItem('userInfo', JSON.stringify(userInfo));
}

// 清除用户信息
function clearUserInfo() {
    localStorage.removeItem('userInfo');
}

// 通用API请求函数
async function apiRequest(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // 添加认证token
    const token = getToken();
    if (token) {
        defaultOptions.headers['Authorization'] = `Bearer ${token}`;
    }

    // 合并选项
    const finalOptions = {
        ...defaultOptions,
        ...options,
        headers: {
            ...defaultOptions.headers,
            ...options.headers,
        },
    };

    try {
        const response = await fetch(`${API_BASE_URL}${url}`, finalOptions);
        
        // 检查响应状态
        if (!response.ok) {
            // 尝试解析错误信息
            let errorMessage = `请求失败，状态码: ${response.status}`;
            try {
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    const errorData = await response.json();
                    errorMessage = errorData.message || errorMessage;
                } else {
                    const errorText = await response.text();
                    errorMessage = `请求失败: ${errorText.substring(0, 100)}`;
                }
            } catch (parseError) {
                // 如果解析失败，使用默认错误信息
                console.warn('无法解析错误响应:', parseError);
            }
            throw new Error(errorMessage);
        }
        
        // 对于成功响应，检查内容类型
        const contentType = response.headers.get('content-type');
        if (!contentType || !contentType.includes('application/json')) {
            // 如果不是JSON响应，尝试获取文本内容
            const text = await response.text();
            console.warn('API返回非JSON响应:', text);
            throw new Error(`API返回非JSON响应: ${text.substring(0, 100)}...`);
        }
        
        // 解析JSON响应
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('API请求错误:', error);
        throw error;
    }
}

// GET请求
function get(url, params = {}) {
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${url}?${queryString}` : url;
    return apiRequest(fullUrl);
}

// POST请求
function post(url, data = {}) {
    return apiRequest(url, {
        method: 'POST',
        body: JSON.stringify(data),
    });
}

// PUT请求
function put(url, data = {}) {
    return apiRequest(url, {
        method: 'PUT',
        body: JSON.stringify(data),
    });
}

// DELETE请求
function del(url) {
    return apiRequest(url, {
        method: 'DELETE',
    });
}

// 用户认证相关API
const authAPI = {
    // 用户登录
    login: (username, password) => {
        return post('/user/login', { username, password });
    },

    // 用户注册
    register: (userData) => {
        return post('/user/register', userData);
    },

    // 管理员登录
    adminLogin: (username, password) => {
        return post('/admin/login', { username, password });
    },

    // 验证token
    validateToken: () => {
        return get('/user/profile');
    },

    // 验证管理员token
    validateAdminToken: () => {
        return get('/admin/profile');
    },
};

// 图书相关API
const bookAPI = {
    // 获取所有图书
    getAllBooks: (params = {}) => {
        return get('/books', params);
    },

    // 根据ID获取图书
    getBookById: (id) => {
        return get(`/books/${id}`);
    },

    // 添加图书（管理员）
    addBook: (bookData) => {
        return post('/books', bookData);
    },

    // 更新图书（管理员）
    updateBook: (id, bookData) => {
        return put(`/books/${id}`, bookData);
    },

    // 删除图书（管理员）
    deleteBook: (id) => {
        return del(`/books/${id}`);
    },
};

// 借阅相关API
const borrowAPI = {
    // 借阅图书
    borrowBook: (bookId) => {
        return post(`/borrow/${bookId}`);
    },

    // 归还图书
    returnBook: (recordId) => {
        return put(`/borrow/return/${recordId}`);
    },

    // 获取用户借阅记录
    getUserBorrowRecords: () => {
        return get('/borrow/user');
    },

    // 获取所有借阅记录（管理员）
    getAllBorrowRecords: () => {
        return get('/borrow/all');
    },
};

// 用户管理API（管理员）
const userAPI = {
    // 获取所有用户
    getAllUsers: () => {
        return get('/admin/users');
    },

    // 根据ID获取用户
    getUserById: (id) => {
        return get(`/admin/users/${id}`);
    },

    // 更新用户信息
    updateUser: (id, userData) => {
        return put(`/admin/users/${id}`, userData);
    },

    // 删除用户
    deleteUser: (id) => {
        return del(`/admin/users/${id}`);
    },
};