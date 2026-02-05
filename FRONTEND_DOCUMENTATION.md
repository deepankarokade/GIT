# Frontend Documentation - Admin Faculty Management System

## 📋 Table of Contents
- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Pages Overview](#pages-overview)
- [Dark Mode Implementation](#dark-mode-implementation)
- [API Integration](#api-integration)
- [JavaScript Patterns](#javascript-patterns)
- [Styling Guide](#styling-guide)
- [Component Reusability](#component-reusability)

---

## 🎨 Overview

The frontend is built with **server-side rendered HTML** using Thymeleaf templates, enhanced with **vanilla JavaScript** for interactivity and **Tailwind CSS** for styling.

### Key Features
- 🌙 **Dark Mode** - System-wide theme with localStorage persistence
- 📱 **Responsive Design** - Mobile-first approach
- ⚡ **Dynamic Content** - Real-time data updates via REST API
- 🎯 **Activity Management** - Visual status indicators and actions
- 🖼️ **Image Upload** - Profile picture with preview
- 🔐 **Password Management** - Manual and random generation

---

## 🛠️ Technology Stack

| Technology | Purpose | CDN/Version |
|-----------|---------|-------------|
| **Tailwind CSS** | Utility-first styling | CDN (latest) |
| **Vanilla JavaScript** | Interactivity | ES6+ |
| **Material Icons** | Icon library | Google Fonts |
| **Lexend Font** | Typography | Google Fonts |
| **Thymeleaf** | Template engine | Spring Boot |

---

## 📁 Project Structure

```
src/main/resources/templates/
├── admin-dashboard.html          # Main dashboard
├── admin-manage-faculty.html     # Faculty list & stats
├── admin-edit-faculty.html       # Edit faculty details
├── admin-add-faculty.html        # Add new faculty
└── admin-manage-profile.html     # Admin profile settings
```

---

## 📄 Pages Overview

### 1. Admin Dashboard (`admin-dashboard.html`)

**Purpose:** Main landing page with overview statistics

**Features:**
- Overview statistics (students, professors, exams)
- Quick action buttons
- Upcoming exams list
- System status indicators
- Dark mode toggle

**Route:** `/admin/dashboard`

---

### 2. Manage Faculty (`admin-manage-faculty.html`)

**Purpose:** List all faculty with search, filter, and bulk actions

**Features:**
- ✅ Live statistics cards (Total, Active, Departments)
- ✅ Faculty table with sorting
- ✅ Search functionality
- ✅ Activity status badges
- ✅ Profile pictures with fallback initials
- ✅ Edit/Delete actions
- ✅ Dark mode support

**Route:** `/admin/dashboard/professor`

**Key Elements:**
```html
<!-- Stats Cards -->
<div id="totalCount">124</div>      <!-- Dynamic count -->
<div id="activeCount">118</div>     <!-- Active faculties -->
<div id="departmentCount">12</div>  <!-- Unique departments -->

<!-- Faculty Table -->
<tbody>
  <!-- Dynamically populated via JavaScript -->
</tbody>
```

**JavaScript Functions:**
```javascript
fetchAllFaculties()      // Load faculty data from API
renderFacultyTable()     // Render table rows
updateStats()            // Update stat cards
searchFaculty()          // Search functionality
```

---

### 3. Edit Faculty (`admin-edit-faculty.html`)

**Purpose:** Edit existing faculty details and manage account status

**Features:**
- ✅ Form pre-filled with existing data
- ✅ Profile picture upload with preview
- ✅ Password update (manual & random generation)
- ✅ Activity status display
- ✅ Account action buttons (Activate/Deactivate/Block/Delete)
- ✅ Real-time status updates
- ✅ Dark mode support

**Route:** `/admin/dashboard/professor/edit/{username}`

**Account Action Buttons:**
```html
<button id="activateBtn">Activate Account</button>
<button id="deactivateBtn">Deactivate Account</button>
<button id="blockBtn">Block User Access</button>
<button id="deleteBtn">Delete Account</button>
```

**Button Visibility Logic:**
- **ACTIVE** → Show Deactivate, Block, Delete
- **INACTIVE/BLOCKED/DELETED** → Show Activate, Block, Delete

**Key Functions:**
```javascript
loadFacultyData()               // Fetch faculty details
updateFaculty()                 // Save changes
updateAccountActionButtons()    // Toggle button visibility
updateStatusDisplay()           // Update status UI
generateRandomPassword()        // Generate secure password
```

---

### 4. Add Faculty (`admin-add-faculty.html`)

**Purpose:** Register new faculty member

**Features:**
- ✅ Complete registration form
- ✅ Profile picture upload (required)
- ✅ Client-side validation
- ✅ File size limits (2MB max)
- ✅ Password field with show/hide
- ✅ Department & subject fields
- ✅ Dark mode support

**Route:** `/admin/dashboard/professor/add`

**Form Validation:**
```javascript
// File size validation
if (file.size > 2 * 1024 * 1024) {
    alert('Photo size must be less than 2MB');
    return;
}

// Email format validation
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
if (!emailRegex.test(email)) {
    alert('Invalid email format');
    return;
}
```

---

### 5. Admin Profile (`admin-manage-profile.html`)

**Purpose:** Manage admin's own profile

**Features:**
- ✅ Profile picture update
- ✅ Name and phone update
- ✅ Password change with validation
- ✅ Password visibility toggles
- ✅ Dark mode support

**Route:** `/admin/profile`

---

## 🌙 Dark Mode Implementation

### Configuration

All pages use Tailwind's `class` strategy for dark mode:

```javascript
// Tailwind Config (in each HTML file)
tailwind.config = {
    darkMode: "class",  // Enable class-based dark mode
    theme: {
        extend: {
            colors: {
                "primary": "#135bec",
                "background-light": "#f6f6f8",
                "background-dark": "#101622"
            }
        }
    }
}
```

### Initialization Script

**Location:** Top of `<script>` section in each page

```javascript
// Dark Mode Initialization
(function initDarkMode() {
    const darkMode = localStorage.getItem('darkMode');
    const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    
    // Apply dark mode if enabled or system prefers dark
    if (darkMode === 'enabled' || (darkMode === null && systemPrefersDark)) {
        document.documentElement.classList.add('dark');
    }
})();
```

### Toggle Implementation

```javascript
// Dark mode toggle button handler
document.addEventListener('DOMContentLoaded', function() {
    const darkModeToggle = document.getElementById('darkModeToggle');
    if (darkModeToggle) {
        darkModeToggle.addEventListener('click', function() {
            document.documentElement.classList.toggle('dark');
            
            // Save preference to localStorage
            if (document.documentElement.classList.contains('dark')) {
                localStorage.setItem('darkMode', 'enabled');
            } else {
                localStorage.setItem('darkMode', 'disabled');
            }
        });
    }
});
```

### Dark Mode Classes

Use Tailwind's `dark:` variant for all elements:

```html
<!-- Text colors -->
<p class="text-slate-900 dark:text-white">Text</p>

<!-- Backgrounds -->
<div class="bg-white dark:bg-slate-900">Content</div>

<!-- Borders -->
<div class="border-gray-200 dark:border-gray-700">Content</div>

<!-- Hover states -->
<button class="hover:bg-gray-100 dark:hover:bg-gray-800">Button</button>
```

### Geometric Background Pattern

```css
.geometric-bg {
    background-image: radial-gradient(#cbd5e1 1px, transparent 1px);
    background-size: 32px 32px;
}

.dark .geometric-bg {
    background-image: radial-gradient(#334155 1px, transparent 1px);
}
```

---

## 🔌 API Integration

### Base URL Configuration

```javascript
const API_BASE_URL = 'http://localhost:8081/admin/faculty';
const BASE_URL = 'http://localhost:8081';
```

### Fetch Pattern

**GET Request:**
```javascript
async function fetchData() {
    try {
        const response = await fetch(`${API_BASE_URL}`);
        
        if (!response.ok) {
            throw new Error('Failed to fetch');
        }
        
        const data = await response.json();
        // Process data
        
    } catch (error) {
        console.error('Error:', error);
        showNotification('Failed to load data', 'error');
    }
}
```

**POST Request with FormData:**
```javascript
async function uploadData() {
    const formData = new FormData();
    formData.append('fullName', 'Dr. John Doe');
    formData.append('email', 'john@example.com');
    formData.append('file', fileInput.files[0]);
    
    try {
        const response = await fetch(`${API_BASE_URL}/register`, {
            method: 'POST',
            body: formData  // Don't set Content-Type header!
        });
        
        if (!response.ok) throw new Error('Upload failed');
        
        showNotification('Success!', 'success');
        
    } catch (error) {
        showNotification('Failed to upload', 'error');
    }
}
```

**PUT Request:**
```javascript
async function updateData() {
    try {
        const response = await fetch(`${API_BASE_URL}/${username}/activate`, {
            method: 'PUT'
        });
        
        if (response.ok) {
            updateUI();
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
```

### Image Loading

**Profile Picture:**
```javascript
async function loadProfilePicture() {
    try {
        const response = await fetch(`${API_BASE_URL}/${username}/profile-picture`);
        
        if (!response.ok) throw new Error('No image');
        
        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        
        // Set image
        profileImg.style.backgroundImage = `url("${url}")`;
        
        // Clean up after 5 seconds
        setTimeout(() => URL.revokeObjectURL(url), 5000);
        
    } catch (error) {
        // Show initials as fallback
        showInitials();
    }
}
```

---

## 💻 JavaScript Patterns

### 1. Notification System

**Usage:**
```javascript
showNotification('Message text', 'type');
```

**Types:**
- `success` - Green notification
- `error` - Red notification  
- `info` - Blue notification

**Implementation:**
```javascript
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `fixed top-4 right-4 px-6 py-3 rounded-lg shadow-lg text-white z-50 ${
        type === 'success' ? 'bg-green-500' :
        type === 'error' ? 'bg-red-500' :
        'bg-blue-500'
    }`;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => notification.remove(), 3000);
}
```

### 2. Avatar Initials Generator

```javascript
function getInitials(name) {
    if (!name) return '?';
    const parts = name.trim().split(' ');
    if (parts.length >= 2) {
        return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return name.substring(0, 2).toUpperCase();
}
```

### 3. Avatar Color Generator

```javascript
function getAvatarColor(name) {
    const colors = [
        'bg-blue-500', 'bg-green-500', 'bg-purple-500',
        'bg-pink-500', 'bg-indigo-500', 'bg-orange-500'
    ];
    const index = name.charCodeAt(0) % colors.length;
    return colors[index];
}
```

### 4. Password Generator

```javascript
function generateRandomPassword() {
    const length = 12;
    const charset = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*';
    let password = '';
    
    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * charset.length);
        password += charset[randomIndex];
    }
    
    return password;
}
```

### 5. Image Preview

```javascript
photoInput.addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (!file) return;
    
    // Validate size
    if (file.size > 2 * 1024 * 1024) {
        alert('File too large!');
        return;
    }
    
    // Show preview
    const reader = new FileReader();
    reader.onload = function(event) {
        previewImg.style.backgroundImage = `url("${event.target.result}")`;
    };
    reader.readAsDataURL(file);
});
```

---

## 🎨 Styling Guide

### Tailwind Utility Classes

#### Colors
```html
<!-- Primary color -->
<div class="bg-primary text-white">Primary Button</div>

<!-- Success -->
<div class="text-green-600 bg-green-50">Success Message</div>

<!-- Error -->
<div class="text-red-600 bg-red-50">Error Message</div>

<!-- Warning -->
<div class="text-orange-600 bg-orange-50">Warning</div>
```

#### Spacing
```html
<!-- Padding -->
<div class="p-4 px-6 py-3">Content</div>

<!-- Margin -->
<div class="m-4 mx-auto mt-8">Content</div>

<!-- Gap (Flexbox/Grid) -->
<div class="flex gap-4">Items</div>
```

#### Borders & Shadows
```html
<!-- Borders -->
<div class="border border-gray-200 rounded-xl">Content</div>

<!-- Shadows -->
<div class="shadow-sm hover:shadow-md transition-shadow">Card</div>
```

#### Responsive Design
```html
<!-- Mobile first approach -->
<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
    <!-- Columns adapt to screen size -->
</div>
```

### Custom CSS

#### Typography
```css
body {
    font-family: 'Lexend', sans-serif;
}
```

#### Scrollbar Styling
```css
.custom-scrollbar::-webkit-scrollbar {
    width: 4px;
}

.custom-scrollbar::-webkit-scrollbar-track {
    background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
    background: #cbd5e1;
    border-radius: 10px;
}
```

---

## 🧩 Component Reusability

### Stat Card Component

```html
<div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-background-dark border border-gray-200 dark:border-gray-800 shadow-sm">
    <p id="count" class="text-3xl font-bold text-slate-900 dark:text-white">0</p>
    <div class="flex items-center gap-3">
        <span class="material-symbols-outlined text-primary bg-primary/10 p-2 rounded-lg">
            groups
        </span>
        <p class="text-sm font-medium text-gray-500 dark:text-gray-400">
            Label Text
        </p>
    </div>
</div>
```

### Profile Picture Component

```html
<div class="relative">
    <div id="profilePic" 
         class="size-32 rounded-full bg-center bg-cover border-4 border-white dark:border-slate-900 shadow-xl"
         style="background-image: url('image.jpg')">
    </div>
    <button id="editBtn"
            class="absolute bottom-0 right-0 p-2 bg-primary text-white rounded-full shadow-lg">
        <span class="material-symbols-outlined text-sm">edit</span>
    </button>
    <input id="fileInput" type="file" accept="image/*" style="display:none">
</div>
```

### Activity Status Badge

```html
<div class="flex items-center gap-1.5">
    <div class="size-2 rounded-full bg-green-500"></div>
    <span class="text-sm font-medium text-green-700 dark:text-green-400">
        Active
    </span>
</div>
```

### Action Button

```html
<button class="group flex items-center justify-between w-full px-4 py-3 rounded-lg border border-primary/30 text-primary font-bold hover:bg-primary hover:text-white transition-all">
    <span class="flex items-center gap-2">
        <span class="material-symbols-outlined">icon_name</span>
        Button Text
    </span>
    <span class="material-symbols-outlined opacity-0 group-hover:opacity-100">
        chevron_right
    </span>
</button>
```

---

## ⚡ Performance Optimization

### Image Optimization
```javascript
// Compress images before upload
function compressImage(file, maxSize) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (e) => {
            const img = new Image();
            img.onload = () => {
                // Compression logic
            };
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);
    });
}
```

### Lazy Loading Images
```html
<img src="placeholder.jpg" data-src="actual-image.jpg" loading="lazy">
```

### Debounce Search
```javascript
function debounce(func, wait) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

// Usage
const debouncedSearch = debounce(searchFaculty, 300);
searchInput.addEventListener('input', debouncedSearch);
```

---

## 🐛 Common Issues & Solutions

### Issue 1: Dark Mode Not Persisting
**Cause:** localStorage not saving  
**Solution:** Check browser privacy settings, ensure script runs before DOM loads

### Issue 2: Images Not Loading
**Cause:** CORS or incorrect path  
**Solution:** 
```javascript
// Add error handling
img.onerror = function() {
    this.src = 'fallback-image.jpg';
};
```

### Issue 3: Form Submission Not Working
**Cause:** Missing CSRF token or incorrect Content-Type  
**Solution:**
```javascript
// For FormData, don't set Content-Type
fetch(url, {
    method: 'POST',
    body: formData  // Browser sets correct Content-Type automatically
});
```

---

## 📱 Responsive Breakpoints

```javascript
// Tailwind default breakpoints
sm: 640px   // @media (min-width: 640px)
md: 768px   // @media (min-width: 768px)
lg: 1024px  // @media (min-width: 1024px)
xl: 1280px  // @media (min-width: 1280px)
2xl: 1536px // @media (min-width: 1536px)
```

**Usage:**
```html
<div class="text-sm md:text-base lg:text-lg">
    Responsive Text
</div>
```

---

## 🔐 Security Best Practices

1. **XSS Prevention:** Always sanitize user input
2. **CSRF Protection:** Include CSRF tokens in forms
3. **File Upload:** Validate file types and sizes
4. **API Keys:** Never expose API keys in frontend code
5. **Input Validation:** Always validate on both client and server

---

## 🚀 Future Enhancements

- [ ] Add infinite scroll for faculty list
- [ ] Implement advanced filtering (by department, status)
- [ ] Add bulk operations (activate multiple, export CSV)
- [ ] Implement real-time updates with WebSocket
- [ ] Add drag-and-drop for file uploads
- [ ] Implement virtual scrolling for large lists
- [ ] Add keyboard shortcuts for power users

---

## 📞 Developer Notes

### Key Files to Modify

**Adding a new field to faculty:**
1. Update `Faculty.java` entity
2. Update registration form in `admin-add-faculty.html`
3. Update edit form in `admin-edit-faculty.html`
4. Update table display in `admin-manage-faculty.html`

**Adding a new page:**
1. Create HTML template in `templates/`
2. Add route in `PageController.java`
3. Add navigation link in sidebar
4. Include dark mode scripts
5. Add to this documentation

### Code Style Guidelines

- Use **camelCase** for JavaScript variables
- Use **kebab-case** for HTML/CSS classes
- Indent with **4 spaces**
- Add comments for complex logic
- Use `const` and `let`, avoid `var`
- Use template literals for strings with variables

---

**Last Updated:** January 15, 2026  
**Version:** 1.0.0  
**Maintainer:** Development Team
