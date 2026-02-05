-- SQL Script to fix the activity_student column
-- Run this in your MySQL database (git_forum)

-- Step 1: Check current data
SELECT id, uid, full_name, activity_student FROM student;

-- Step 2: Add a temporary column
ALTER TABLE student ADD COLUMN activity_student_temp VARCHAR(20);

-- Step 3: Copy and convert the data (assuming current values are 0, 1, 2)
UPDATE student 
SET activity_student_temp = CASE 
    WHEN activity_student = 0 THEN 'ACTIVE'
    WHEN activity_student = 1 THEN 'INACTIVE'
    WHEN activity_student = 2 THEN 'DELETED'
    ELSE 'ACTIVE'  -- Default to ACTIVE if NULL or other value
END;

-- Step 4: Drop the old column
ALTER TABLE student DROP COLUMN activity_student;

-- Step 5: Rename the temp column to the original name
ALTER TABLE student CHANGE COLUMN activity_student_temp activity_student VARCHAR(20);

-- Step 6: Set default value for new records
ALTER TABLE student ALTER COLUMN activity_student SET DEFAULT 'ACTIVE';

-- Step 7: Verify the changes
SELECT id, uid, full_name, activity_student FROM student;
