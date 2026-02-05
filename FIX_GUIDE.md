# 🔧 Fix Guide: ActivityStudent Enum Database Error

## Problem
You changed the `ActivityStudent` enum from storing as integers (0, 1, 2) to strings (ACTIVE, INACTIVE, DELETED), 
but your database still has integer values in the `activity_student` column.

## Error
The application fails to start because Hibernate can't convert integer values to the enum string values.

---

## Solutions (Choose ONE)

### ✅ **Option 1: SQL Migration (RECOMMENDED - Preserves Data)**

**Steps:**
1. Open MySQL Workbench or command line
2. Connect to your `git_forum` database
3. Run the SQL script: `fix_activity_column.sql`
4. Restart your Spring Boot application

**Advantages:**
- ✅ Keeps all existing student data
- ✅ Safe for production
- ✅ Professional approach

---

### ⚠️ **Option 2: Drop and Recreate (FOR DEVELOPMENT ONLY)**

**WARNING: This will DELETE ALL student data!**

**Steps:**
1. **Backup:** If you have any important data, export it first!

2. **Temporary Change:** Edit `src/main/resources/application.properties`:
   ```properties
   # Change this line from:
   spring.jpa.hibernate.ddl-auto=update
   
   # To:
   spring.jpa.hibernate.ddl-auto=create
   ```

3. **Restart:** Run your application ONCE - it will drop and recreate all tables

4. **Revert:** Change back to:
   ```properties
   spring.jpa.hibernate.ddl-auto=update
   ```

5. **Restart again:** Now your application should work normally

**Advantages:**
- ✅ Quick and simple
- ✅ No SQL knowledge needed

**Disadvantages:**
- ❌ DELETES ALL DATA
- ❌ Only suitable for development

---

### 🔄 **Option 3: Manual Database Cleanup**

If you don't care about existing student data:

**SQL Commands:**
```sql
-- Connect to your database
USE git_forum;

-- Option A: Drop just the student table
DROP TABLE IF EXISTS student;

-- Then restart your application (with ddl-auto=update)
-- It will create the table with the correct schema
```

---

## Recommended Approach

**For Development:**
- Use **Option 2** (Drop and Recreate) if you don't have important data

**For Production or with Important Data:**
- Use **Option 1** (SQL Migration Script)

---

## After Fixing

Once you've fixed the database:
1. Restart your Spring Boot application
2. The `activity_student` column will now store: `ACTIVE`, `INACTIVE`, or `DELETED`
3. All new student records will use string values by default

---

## Verify Success

After restarting, check your database:
```sql
SELECT uid, full_name, activity_student FROM student;
```

You should see values like:
```
+----------+--------------+------------------+
| uid      | full_name    | activity_student |
+----------+--------------+------------------+
| STU001   | John Doe     | ACTIVE          |
| STU002   | Jane Smith   | INACTIVE        |
+----------+--------------+------------------+
```

✅ **Success!** Your enum values are now stored as readable strings!
