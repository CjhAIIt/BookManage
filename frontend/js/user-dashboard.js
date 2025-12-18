// DOM元素
const booksTab = document.getElementById('books-tab');
const borrowRecordsTab = document.getElementById('borrow-records-tab');
const booksSection = document.getElementById('books-section');
const borrowRecordsSection = document.getElementById('borrow-records-section');
const userName = document.getElementById('user-name');
const logoutBtn = document.getElementById('logout-btn');
const bookSearch = document.getElementById('book-search');
const searchBtn = document.getElementById('search-btn');
const booksList = document.getElementById('books-list');
const borrowRecordsList = document.getElementById('borrow-records-list');

// 初始化
document.addEventListener('DOMContentLoaded', function() {
    // 获取本地存储的用户信息
    const userInfo = getUserInfo();
    if (userInfo) {
        userName.textContent = userInfo.username;
    }
    
    // 加载数据
    loadBooks();
    loadBorrowRecords();
    
    // 绑定事件
    booksTab.addEventListener('click', showBooksSection);
    borrowRecordsTab.addEventListener('click', showBorrowRecordsSection);
    logoutBtn.addEventListener('click', logout);
    searchBtn.addEventListener('click', searchBooks);
    bookSearch.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            searchBooks();
        }
    });
});

// 显示图书列表区域
function showBooksSection() {
    booksTab.classList.add('active');
    borrowRecordsTab.classList.remove('active');
    booksSection.classList.add('active');
    borrowRecordsSection.classList.remove('active');
}

// 显示借阅记录区域
function showBorrowRecordsSection() {
    booksTab.classList.remove('active');
    borrowRecordsTab.classList.add('active');
    booksSection.classList.remove('active');
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
    
    books.forEach(book => {
        const bookCard = document.createElement('div');
        bookCard.className = 'book-card';
        
        // 根据库存判断图书状态
        const statusClass = (book.stock && book.stock > 0) ? 'available' : 'borrowed';
        const statusText = (book.stock && book.stock > 0) ? '可借阅' : '已借出';
        
        let actionButton = '';
        if (book.stock && book.stock > 0) {
            actionButton = `<button class="btn btn-small btn-borrow" onclick="borrowBook(${book.id})">借阅</button>`;
        } else {
            actionButton = `<span class="book-status borrowed">已借出</span>`;
        }
        
        bookCard.innerHTML = `
            <div class="book-title">${book.title}</div>
            <div class="book-author">作者: ${book.author}</div>
            <div class="book-isbn">ISBN: ${book.isbn}</div>
            <div class="book-status ${statusClass}">${statusText}</div>
            <div class="book-actions">
                ${actionButton}
            </div>
        `;
        
        booksList.appendChild(bookCard);
    });
}

// 搜索图书
function searchBooks() {
    const searchQuery = bookSearch.value.trim();
    loadBooks(searchQuery);
}

// 借阅图书
async function borrowBook(bookId) {
    try {
        const response = await borrowAPI.borrowBook(bookId);
        
        if (response.code === 200) {
            showMessage('借阅成功', 'success');
            // 刷新图书列表
            loadBooks(bookSearch.value.trim());
            // 刷新借阅记录
            loadBorrowRecords();
        } else {
            showMessage(response.message || '借阅失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '借阅失败，请稍后重试', 'error');
    }
}

// 加载借阅记录
async function loadBorrowRecords() {
    try {
        const response = await borrowAPI.getUserBorrowRecords();
        
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
                <th>图书名称</th>
                <th>作者</th>
                <th>借阅时间</th>
                <th>应还时间</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    `;
    
    const tbody = table.querySelector('tbody');
    
    records.forEach(record => {
        const row = document.createElement('tr');
        
        const statusText = formatBorrowStatus(record.status);
        let actionButton = '';
        
        if (record.status === 'BORROWED') {
            actionButton = `<button class="btn btn-small btn-return" onclick="returnBook(${record.id})">归还</button>`;
        } else {
            actionButton = `<span class="book-status">${statusText}</span>`;
        }
        
        row.innerHTML = `
            <td>${record.bookTitle}</td>
            <td>${record.bookAuthor}</td>
            <td>${formatDate(record.borrowDate)}</td>
            <td>${formatDate(record.dueDate)}</td>
            <td>${statusText}</td>
            <td>${actionButton}</td>
        `;
        
        tbody.appendChild(row);
    });
    
    borrowRecordsList.appendChild(table);
}

// 归还图书
async function returnBook(recordId) {
    try {
        const response = await borrowAPI.returnBook(recordId);
        
        if (response.code === 200) {
            showMessage('归还成功', 'success');
            // 刷新借阅记录
            loadBorrowRecords();
            // 刷新图书列表
            loadBooks(bookSearch.value.trim());
        } else {
            showMessage(response.message || '归还失败', 'error');
        }
    } catch (error) {
        showMessage(error.message || '归还失败，请稍后重试', 'error');
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