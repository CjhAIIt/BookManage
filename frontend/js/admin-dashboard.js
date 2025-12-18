// DOM元素
const booksTab = document.getElementById('books-tab');
const usersTab = document.getElementById('users-tab');
const borrowRecordsTab = document.getElementById('borrow-records-tab');
const booksSection = document.getElementById('books-section');
const usersSection = document.getElementById('users-section');
const borrowRecordsSection = document.getElementById('borrow-records-section');
const adminName = document.getElementById('admin-name');
const logoutBtn = document.getElementById('logout-btn');
const bookSearch = document.getElementById('book-search');
const searchBtn = document.getElementById('search-btn');
const booksList = document.getElementById('books-list');
const addBookBtn = document.getElementById('add-book-btn');
const bookModal = document.getElementById('book-modal');
const bookModalTitle = document.getElementById('book-modal-title');
const bookForm = document.getElementById('book-form');
const bookId = document.getElementById('book-id');
const bookTitle = document.getElementById('book-title');
const bookAuthor = document.getElementById('book-author');
const bookIsbn = document.getElementById('book-isbn');
const bookDescription = document.getElementById('book-description');
const bookStock = document.getElementById('book-stock');
const userSearch = document.getElementById('user-search');
const userSearchBtn = document.getElementById('user-search-btn');
const usersList = document.getElementById('users-list');
const userModal = document.getElementById('user-modal');
const userForm = document.getElementById('user-form');
const userId = document.getElementById('user-id');
const userUsername = document.getElementById('user-username');
const userEmail = document.getElementById('user-email');
const userRole = document.getElementById('user-role');
const borrowRecordsList = document.getElementById('borrow-records-list');

// 初始化
document.addEventListener('DOMContentLoaded', function() {
    // 获取本地存储的管理员信息
    const userInfo = getUserInfo();
    if (userInfo) {
        adminName.textContent = userInfo.username;
    }
    
    // 加载数据
    loadBooks();
    loadUsers();
    loadBorrowRecords();
    
    // 绑定事件
    booksTab.addEventListener('click', showBooksSection);
    usersTab.addEventListener('click', showUsersSection);
    borrowRecordsTab.addEventListener('click', showBorrowRecordsSection);
    logoutBtn.addEventListener('click', logout);
    searchBtn.addEventListener('click', searchBooks);
    bookSearch.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            searchBooks();
        }
    });
    addBookBtn.addEventListener('click', showAddBookModal);
    bookForm.addEventListener('submit', handleBookSubmit);
    userSearchBtn.addEventListener('click', searchUsers);
    userSearch.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            searchUsers();
        }
    });
    userForm.addEventListener('submit', handleUserSubmit);
    
    // 模态框关闭事件
    const closeButtons = document.querySelectorAll('.close');
    closeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const modal = this.closest('.modal');
            modal.style.display = 'none';
        });
    });
    
    const cancelButtons = document.querySelectorAll('.cancel-btn');
    cancelButtons.forEach(button => {
        button.addEventListener('click', function() {
            const modal = this.closest('.modal');
            modal.style.display = 'none';
        });
    });
    
    // 点击模态框外部关闭
    window.addEventListener('click', function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    });
});

// 显示图书管理区域
function showBooksSection() {
    booksTab.classList.add('active');
    usersTab.classList.remove('active');
    borrowRecordsTab.classList.remove('active');
    booksSection.classList.add('active');
    usersSection.classList.remove('active');
    borrowRecordsSection.classList.remove('active');
}

// 显示用户管理区域
function showUsersSection() {
    booksTab.classList.remove('active');
    usersTab.classList.add('active');
    borrowRecordsTab.classList.remove('active');
    booksSection.classList.remove('active');
    usersSection.classList.add('active');
    borrowRecordsSection.classList.remove('active');
}

// 显示借阅记录区域
function showBorrowRecordsSection() {
    booksTab.classList.remove('active');
    usersTab.classList.remove('active');
    borrowRecordsTab.classList.add('active');
    booksSection.classList.remove('active');
    usersSection.classList.remove('active');
    borrowRecordsSection.classList.add('active');
}

// 加载图书列表
async function loadBooks(searchQuery = '') {
    try {
        const params = {};
        if (searchQuery) {
            params.search = searchQuery;
        }
        
        const response = await bookAPI.getAllBooks(params);
        
        if (response.code === 200) {
            renderBooksList(response.data);
        } else {
            showMessage(response.message || '加载图书列表失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '加载图书列表失败，请稍后重试', 'error');
    }
}

// 渲染图书列表
function renderBooksList(books) {
    booksList.innerHTML = '';
    
    if (!books || books.length === 0) {
        booksList.innerHTML = '<p>暂无图书数据</p>';
        return;
    }
    
    const table = document.createElement('table');
    table.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>书名</th>
                <th>作者</th>
                <th>ISBN</th>
                <th>库存</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    `;
    
    const tbody = table.querySelector('tbody');
    
    books.forEach(book => {
        const row = document.createElement('tr');
        
        const statusClass = (book.stock && book.stock > 0) ? 'available' : 'borrowed';
        const statusText = (book.stock && book.stock > 0) ? '可借阅' : '已借完';
        
        row.innerHTML = `
            <td>${book.id}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.isbn}</td>
            <td>${book.stock || 0}</td>
            <td><span class="book-status ${statusClass}">${statusText}</span></td>
            <td>
                <button class="btn btn-small" onclick="editBook(${book.id})">编辑</button>
                <button class="btn btn-small btn-danger" onclick="deleteBook(${book.id})">删除</button>
            </td>
        `;
        
        tbody.appendChild(row);
    });
    
    booksList.appendChild(table);
}

// 搜索图书
function searchBooks() {
    const searchQuery = bookSearch.value.trim();
    loadBooks(searchQuery);
}

// 显示添加图书模态框
function showAddBookModal() {
    bookModalTitle.textContent = '添加图书';
    bookForm.reset();
    bookId.value = '';
    bookModal.style.display = 'block';
}

// 编辑图书
async function editBook(id) {
    try {
        const response = await bookAPI.getBookById(id);
        
        if (response.code === 200) {
            const book = response.data;
            bookModalTitle.textContent = '编辑图书';
            bookId.value = book.id;
            bookTitle.value = book.title;
            bookAuthor.value = book.author;
            bookIsbn.value = book.isbn;
            bookDescription.value = book.description || '';
            bookStock.value = book.stock || 0;
            bookModal.style.display = 'block';
        } else {
            showMessage(response.message || '获取图书信息失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '获取图书信息失败，请稍后重试', 'error');
    }
}

// 处理图书表单提交
async function handleBookSubmit(event) {
    event.preventDefault();
    
    const bookData = {
        title: bookTitle.value,
        author: bookAuthor.value,
        isbn: bookIsbn.value,
        description: bookDescription.value,
        stock: parseInt(bookStock.value) || 0,
        status: 'AVAILABLE'
    };
    
    try {
        let response;
        if (bookId.value) {
            // 更新图书
            response = await bookAPI.updateBook(bookId.value, bookData);
        } else {
            // 添加图书
            response = await bookAPI.addBook(bookData);
        }
        
        if (response.code === 200) {
            showMessage(bookId.value ? '更新成功' : '添加成功', 'success');
            bookModal.style.display = 'none';
            // 刷新图书列表
            loadBooks(bookSearch.value.trim());
        } else {
            showMessage(response.message || '操作失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '操作失败，请稍后重试', 'error');
    }
}

// 删除图书
async function deleteBook(id) {
    if (!confirm('确定要删除这本图书吗？')) {
        return;
    }
    
    try {
        const response = await bookAPI.deleteBook(id);
        
        if (response.code === 200) {
            showMessage('删除成功', 'success');
            // 刷新图书列表
            loadBooks(bookSearch.value.trim());
        } else {
            showMessage(response.message || '删除失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '删除失败，请稍后重试', 'error');
    }
}

// 加载用户列表
async function loadUsers(searchQuery = '') {
    try {
        const response = await userAPI.getAllUsers();
        
        if (response.code === 200) {
            let users = response.data;
            
            // 如果有搜索查询，过滤用户列表
            if (searchQuery) {
                users = users.filter(user => 
                    user.username.toLowerCase().includes(searchQuery.toLowerCase()) ||
                    user.email.toLowerCase().includes(searchQuery.toLowerCase())
                );
            }
            
            renderUsersList(users);
        } else {
            showMessage(response.message || '加载用户列表失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '加载用户列表失败，请稍后重试', 'error');
    }
}

// 渲染用户列表
function renderUsersList(users) {
    usersList.innerHTML = '';
    
    if (!users || users.length === 0) {
        usersList.innerHTML = '<p>暂无用户数据</p>';
        return;
    }
    
    const table = document.createElement('table');
    table.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>用户名</th>
                <th>邮箱</th>
                <th>角色</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    `;
    
    const tbody = table.querySelector('tbody');
    
    users.forEach(user => {
        const row = document.createElement('tr');
        
        const roleText = user.role === 'ADMIN' ? '管理员' : '普通用户';
        
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${roleText}</td>
            <td>
                <button class="btn btn-small" onclick="editUser(${user.id})">编辑</button>
                <button class="btn btn-small btn-danger" onclick="deleteUser(${user.id})">删除</button>
            </td>
        `;
        
        tbody.appendChild(row);
    });
    
    usersList.appendChild(table);
}

// 搜索用户
function searchUsers() {
    const searchQuery = userSearch.value.trim();
    loadUsers(searchQuery);
}

// 编辑用户
async function editUser(id) {
    try {
        const response = await userAPI.getUserById(id);
        
        if (response.code === 200) {
            const user = response.data;
            userId.value = user.id;
            userUsername.value = user.username;
            userEmail.value = user.email;
            userRole.value = user.role;
            userModal.style.display = 'block';
        } else {
            showMessage(response.message || '获取用户信息失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '获取用户信息失败，请稍后重试', 'error');
    }
}

// 处理用户表单提交
async function handleUserSubmit(event) {
    event.preventDefault();
    
    const userData = {
        username: userUsername.value,
        email: userEmail.value,
        role: userRole.value
    };
    
    try {
        const response = await userAPI.updateUser(userId.value, userData);
        
        if (response.code === 200) {
            showMessage('更新成功', 'success');
            userModal.style.display = 'none';
            // 刷新用户列表
            loadUsers(userSearch.value.trim());
        } else {
            showMessage(response.message || '更新失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '更新失败，请稍后重试', 'error');
    }
}

// 删除用户
async function deleteUser(id) {
    if (!confirm('确定要删除这个用户吗？删除后该用户的所有借阅记录也将被删除。')) {
        return;
    }
    
    try {
        const response = await userAPI.deleteUser(id);
        
        if (response.code === 200) {
            showMessage('删除成功', 'success');
            // 刷新用户列表
            loadUsers(userSearch.value.trim());
        } else {
            showMessage(response.message || '删除失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '删除失败，请稍后重试', 'error');
    }
}

// 加载借阅记录
async function loadBorrowRecords() {
    try {
        const response = await borrowAPI.getAllBorrowRecords();
        
        if (response.code === 200) {
            renderBorrowRecordsList(response.data);
        } else {
            showMessage(response.message || '加载借阅记录失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '加载借阅记录失败，请稍后重试', 'error');
    }
}

// 渲染借阅记录列表
function renderBorrowRecordsList(records) {
    borrowRecordsList.innerHTML = '';
    
    if (!records || records.length === 0) {
        borrowRecordsList.innerHTML = '<p>暂无借阅记录</p>';
        return;
    }
    
    const table = document.createElement('table');
    table.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>用户名</th>
                <th>图书名称</th>
                <th>作者</th>
                <th>借阅时间</th>
                <th>应还时间</th>
                <th>归还时间</th>
                <th>状态</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    `;
    
    const tbody = table.querySelector('tbody');
    
    records.forEach(record => {
        const row = document.createElement('tr');
        
        const statusText = formatBorrowStatus(record.status);
        const returnTime = record.returnDate ? formatDate(record.returnDate) : '-';
        
        row.innerHTML = `
            <td>${record.id}</td>
            <td>${record.userName}</td>
            <td>${record.bookTitle}</td>
            <td>${record.bookAuthor}</td>
            <td>${formatDate(record.borrowDate)}</td>
            <td>${formatDate(record.dueDate)}</td>
            <td>${returnTime}</td>
            <td>${statusText}</td>
        `;
        
        tbody.appendChild(row);
    });
    
    borrowRecordsList.appendChild(table);
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