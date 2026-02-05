# Backend Documentation - Admin Faculty Management System

## 📋 Table of Contents
- [Architecture Overview](#architecture-overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Entity Models](#entity-models)
- [API Endpoints](#api-endpoints)
- [Service Layer](#service-layer)
- [Activity Status System](#activity-status-system)
- [Database Schema](#database-schema)

---

## 🏗️ Architecture Overview

The backend follows a **layered architecture** pattern:

```
┌──────────────────┐
│   Controllers    │  ← REST API & Page Routing
├──────────────────┤
│   Services       │  ← Business Logic
├──────────────────┤
│   Repositories   │  ← Data Access Layer
├──────────────────┤
│   Entities       │  ← Domain Models
└──────────────────┘
```

### Package Structure
```
com.git.Admin
├── Controller
│   ├── FacultyController.java    # REST API for faculty operations
│   └── PageController.java       # View routing
├── Service
│   └── FacultyService.java       # Business logic
├── Repository
│   └── FacultyRepository.java    # JPA data access
├── Entity
│   └── Faculty.java              # Faculty domain model
└── Activity.java                 # Activity status enum
```

---

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 3.4.0 |
| **Language** | Java | 17 |
| **ORM** | JPA/Hibernate | - |
| **Database** | MySQL/PostgreSQL | - |
| **Build Tool** | Maven/Gradle | - |

---

## 📁 Project Structure

```
src/main/
├── java/com/git/
│   └── Admin/
│       ├── Controller/
│       │   ├── FacultyController.java
│       │   └── PageController.java
│       ├── Service/
│       │   └── FacultyService.java
│       ├── Repository/
│       │   └── FacultyRepository.java
│       ├── Entity/
│       │   └── Faculty.java
│       └── Activity.java
└── resources/
    ├── templates/          # Thymeleaf HTML templates
    │   ├── admin-dashboard.html
    │   ├── admin-manage-faculty.html
    │   ├── admin-edit-faculty.html
    │   ├── admin-add-faculty.html
    │   └── admin-manage-profile.html
    └── application.properties
```

---

## 📊 Entity Models

### Faculty Entity

**File:** `src/main/java/com/git/Admin/Entity/Faculty.java`

```java
@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;        // Unique identifier
    private String password;        // Encrypted password
    private String fullName;        // Display name
    private String email;           // Contact email
    private String phoneNumber;     // Contact phone
    private String department;      // Academic department
    private String qualification;   // Educational qualifications
    private String subject;         // Teaching subject
    private String photofilename;   // Profile picture filename
    private boolean termsAccepted;  // Terms agreement status
    
    @Enumerated(EnumType.STRING)
    private Activity activity = Activity.ACTIVE;  // Account status
}
```

### Activity Enum

**File:** `src/main/java/com/git/Admin/Activity.java`

```java
public enum Activity {
    ACTIVE,      // Account is active and can log in
    INACTIVE,    // Account is temporarily disabled
    BLOCKED,     // Account is restricted (stronger than inactive)
    DELETED      // Account is marked as deleted (soft delete)
}
```

---

## 🔌 API Endpoints

### Base URL
```
http://localhost:8081/admin/faculty
```

### Faculty CRUD Operations

#### 1. Get All Faculty
```http
GET /admin/faculty
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "username": "PROF-2024-001",
    "fullName": "Dr. John Smith",
    "email": "john.smith@university.edu",
    "department": "Computer Science",
    "subject": "Data Structures",
    "activity": "ACTIVE",
    "photofilename": "prof_001.jpg"
  }
]
```

---

#### 2. Get Faculty by Username
```http
GET /admin/faculty/{username}
```

**Parameters:**
- `username` (path) - Faculty username

**Response:** `200 OK`
```json
{
  "id": 1,
  "username": "PROF-2024-001",
  "fullName": "Dr. John Smith",
  "email": "john.smith@university.edu",
  "activity": "ACTIVE"
}
```

---

#### 3. Register New Faculty
```http
POST /admin/faculty/register
```

**Content-Type:** `multipart/form-data`

**Form Data:**
```
username: PROF-2024-002
password: SecurePass123!
fullName: Dr. Jane Doe
email: jane.doe@university.edu
phoneNumber: 1234567890
department: Mathematics
qualification: PhD in Mathematics
subject: Calculus
document: [file] (profile picture)
termsAccepted: true
```

**Response:** `200 OK`
```json
{
  "id": 2,
  "username": "PROF-2024-002",
  "fullName": "Dr. Jane Doe"
}
```

---

#### 4. Update Faculty Details
```http
PUT /admin/faculty/{username}
```

**Content-Type:** `multipart/form-data`

**Form Data:**
```
fullName: Dr. Jane Smith
email: jane.smith@university.edu
phoneNumber: 9876543210
department: Mathematics
qualification: PhD in Applied Math
subject: Linear Algebra
password: NewPassword123 (optional)
```

**Response:** `200 OK`

---

#### 5. Delete Faculty
```http
DELETE /admin/faculty/{username}
```

**Response:** `200 OK`
```
Faculty deleted successfully
```

---

### Profile Picture Management

#### 6. Get Profile Picture
```http
GET /admin/faculty/{username}/profile-picture
```

**Response:** `200 OK`
- **Content-Type:** `image/jpeg`
- **Body:** Binary image data

---

#### 7. Update Profile Picture
```http
POST /admin/faculty/{username}/profile-picture
```

**Content-Type:** `multipart/form-data`

**Form Data:**
```
file: [image file]
```

**Validation:**
- Maximum file size: 2MB
- Supported formats: JPEG, PNG

**Response:** `200 OK`
```
Profile picture updated successfully
```

---

### Activity Status Management

#### 8. Get Activity Status
```http
GET /admin/faculty/{username}/status
```

**Response:** `200 OK`
```
ACTIVE
```

---

#### 9. Activate Account
```http
PUT /admin/faculty/{username}/activate
```

**Response:** `200 OK`
- Sets `activity` to `ACTIVE`

---

#### 10. Deactivate Account
```http
PUT /admin/faculty/{username}/deactivate
```

**Response:** `200 OK`
- Sets `activity` to `INACTIVE`

---

#### 11. Block Account
```http
PUT /admin/faculty/{username}/block
```

**Response:** `200 OK`
- Sets `activity` to `BLOCKED`

---

#### 12. Mark as Deleted
```http
PUT /admin/faculty/{username}/delete
```

**Response:** `200 OK`
- Sets `activity` to `DELETED`
- **Note:** This is a soft delete (record remains in database)

---

## 🔧 Service Layer

### FacultyService

**File:** `src/main/java/com/git/Admin/Service/FacultyService.java`

#### Key Methods

```java
public class FacultyService {
    
    // CRUD Operations
    List<Faculty> getAllFaculties();
    Faculty getFacultyByUsername(String username);
    Faculty registerFaculty(Faculty faculty, MultipartFile document);
    Faculty updateFaculty(String username, Faculty updatedFaculty);
    void deleteFaculty(String username);
    
    // Profile Picture Management
    byte[] getProfilePicture(String username);
    void updateProfilePicture(String username, MultipartFile file);
    
    // Activity Status Management
    Activity getAccountStatus(String username);
    void updateAccountStatus(String username, Activity activity);
}
```

#### Business Rules

1. **Username Uniqueness:** Each faculty must have a unique username
2. **Password Updates:** Password is only updated if provided in the request
3. **Profile Pictures:** Stored in the file system with reference in database
4. **Activity Status:** Defaults to `ACTIVE` on registration
5. **Soft Delete:** Delete operations set status to `DELETED` instead of removing records

---

## 🎯 Activity Status System

### Status Flow Diagram

```
┌─────────┐
│ ACTIVE  │ ←──┐
└────┬────┘    │
     │         │
     ↓         │
┌─────────┐   │ Activate
│INACTIVE │───┤
└────┬────┘   │
     │         │
     ↓         │
┌─────────┐   │
│ BLOCKED │───┤
└────┬────┘   │
     │         │
     ↓         │
┌─────────┐   │
│ DELETED │───┘
└─────────┘
```

### Status Meanings

| Status | Description | Can Login? | Visible in List? |
|--------|-------------|------------|------------------|
| **ACTIVE** | Account is fully functional | ✅ Yes | ✅ Yes |
| **INACTIVE** | Temporarily disabled | ❌ No | ✅ Yes |
| **BLOCKED** | Restricted access | ❌ No | ✅ Yes |
| **DELETED** | Soft deleted | ❌ No | ⚠️ Maybe |

### State Transitions

- Any status can transition to **ACTIVE** via `/activate`
- **ACTIVE** → **INACTIVE** via `/deactivate`
- **ACTIVE** → **BLOCKED** via `/block`
- Any status → **DELETED** via `/delete`

---

## 🗄️ Database Schema

### Faculty Table

```sql
CREATE TABLE faculty (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    department VARCHAR(100),
    qualification VARCHAR(255),
    subject VARCHAR(100),
    photofilename VARCHAR(255),
    terms_accepted BOOLEAN DEFAULT false,
    activity VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Indexes

```sql
CREATE INDEX idx_username ON faculty(username);
CREATE INDEX idx_email ON faculty(email);
CREATE INDEX idx_activity ON faculty(activity);
CREATE INDEX idx_department ON faculty(department);
```

---

## 🔐 Security Considerations

1. **Password Storage:** Passwords should be hashed using BCrypt
2. **Authentication:** Implement JWT or session-based auth
3. **Authorization:** Verify admin permissions for all endpoints
4. **File Upload Validation:** 
   - Check file types
   - Limit file sizes
   - Sanitize filenames
5. **SQL Injection:** Using JPA protects against SQL injection
6. **CORS:** Configure CORS if frontend is on different domain

---

## 🧪 Testing Recommendations

### Unit Tests
```java
@SpringBootTest
class FacultyServiceTest {
    
    @Test
    void testRegisterFaculty() {
        // Test faculty registration
    }
    
    @Test
    void testUpdateActivityStatus() {
        // Test status transitions
    }
}
```

### Integration Tests
```java
@SpringBootTest
@AutoConfigureMockMvc
class FacultyControllerTest {
    
    @Test
    void testGetAllFaculties() throws Exception {
        mockMvc.perform(get("/admin/faculty"))
            .andExpect(status().isOk());
    }
}
```

---

## 📝 Common Issues & Solutions

### Issue 1: Profile Picture Not Loading
**Cause:** Incorrect file path or permissions  
**Solution:** Verify file storage directory and read permissions

### Issue 2: Duplicate Username
**Cause:** Trying to register with existing username  
**Solution:** Implement username validation before registration

### Issue 3: Activity Status Not Updating
**Cause:** Enum mismatch or database column type  
**Solution:** Ensure database column is VARCHAR and JPA mapping uses `@Enumerated(EnumType.STRING)`

---

## 🚀 Future Enhancements

- [ ] Add email notifications for status changes
- [ ] Implement audit logging for all operations
- [ ] Add batch import/export functionality
- [ ] Implement role-based access control
- [ ] Add faculty search and filtering
- [ ] Implement password reset workflow
- [ ] Add course assignment management

---

## 📞 Support

For questions or issues, please contact:
- **Email:** deepankarr@technocipher.co.in
- **GitHub:** github.com/deepankarokade

---

**Last Updated:** January 15, 2026  
**Version:** 1.0.0
