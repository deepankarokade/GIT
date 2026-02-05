/**
 * Application Configuration
 * 
 * IMPORTANT: Change the BASE_URL below when deploying to a different server/port.
 * This is the ONLY file you need to modify when changing the server URL.
 * 
 * Examples:
 *   - Local development: 'http://localhost:8080'
 *   - Different port: 'http://localhost:8081'
 *   - Production: 'https://your-domain.com'
 *   - Empty string '' for relative URLs (recommended for production)
 */

// ============================================
// ⚙️ CHANGE THIS URL WHEN SWITCHING SYSTEMS
// ============================================
const BASE_URL = '';  // Empty string means relative URLs (uses current domain/port)

// Alternative: Use absolute URL if needed
// const BASE_URL = 'http://localhost:8080';

// ============================================
// API Endpoints (derived from BASE_URL)
// ============================================
const API_ENDPOINTS = {
    // Admin endpoints
    ADMIN_LOGIN: `${BASE_URL}/admin/login`,
    ADMIN_DASHBOARD: `${BASE_URL}/admin/dashboard`,
    ADMIN_PROFILE: `${BASE_URL}/admin/profile`,
    ADMIN_FACULTY: `${BASE_URL}/admin/faculty`,
    ADMIN_STUDENT: `${BASE_URL}/admin/student`,
    ADMIN_COURSE: `${BASE_URL}/admin/course`,
    ADMIN_PAYMENT: `${BASE_URL}/admin/payment`,

    // Student endpoints
    STUDENT_LOGIN: `${BASE_URL}/student/login`,
    STUDENT_DASHBOARD: `${BASE_URL}/student/dashboard`,
    STUDENT_REGISTER: `${BASE_URL}/admin/student/register`,

    // Professor endpoints
    PROFESSOR_LOGIN: `${BASE_URL}/professor/login`,
    PROFESSOR_DASHBOARD: `${BASE_URL}/professor/dashboard`,

    // Payment endpoints
    PAYMENT: `${BASE_URL}/admin/payment`
};

// Helper function to construct full API URL
function getApiUrl(endpoint) {
    return `${BASE_URL}${endpoint}`;
}

// Helper function for navigation
function navigateTo(path) {
    window.location.href = `${BASE_URL}${path}`;
}

// Export for use in modules (if using ES6 modules)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { BASE_URL, API_ENDPOINTS, getApiUrl, navigateTo };
}
