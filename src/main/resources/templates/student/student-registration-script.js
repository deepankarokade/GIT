// Photo upload functionality
const photoInput = document.getElementById('photoInput');
const uploadPhotoBtn = document.getElementById('uploadPhotoBtn');
const photoPreviewContainer = document.getElementById('photoPreviewContainer');

uploadPhotoBtn.addEventListener('click', () => {
    photoInput.click();
});

photoInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file) {
        // Validate file size (2MB max)
        if (file.size > 2 * 1024 * 1024) {
            alert('File size must be less than 2MB');
            photoInput.value = '';
            return;
        }

        // Validate file type
        if (!['image/jpeg', 'image/png'].includes(file.type)) {
            alert('Only JPG and PNG files are allowed');
            photoInput.value = '';
            return;
        }

        // Preview the image
        const reader = new FileReader();
        reader.onload = (e) => {
            photoPreviewContainer.innerHTML = `<img src="${e.target.result}" alt="Preview" class="w-full h-full object-cover" />`;
        };
        reader.readAsDataURL(file);
    }
});

// Form submission
const form = document.getElementById('studentRegistrationForm');
// BASE_URL comes from /js/config.js (must be loaded before this script)

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Get all form inputs using name attributes (more reliable)
    const formData = new FormData();

    // Personal Information
    const fullName = form.querySelector('input[name="fullName"]').value;
    const dob = form.querySelector('input[name="dob"]').value;
    const gender = form.querySelector('input[name="gender"]:checked')?.value || '';

    // Contact Details - using name attributes
    const email = form.querySelector('input[name="email"]')?.value || '';
    const contactNumber = form.querySelector('input[name="contactNumber"]')?.value || '';
    const address = form.querySelector('input[name="address"]')?.value || '';
    const state = form.querySelector('select[name="state"]')?.value || '';
    const city = form.querySelector('input[name="city"]')?.value || '';

    // Parent/Guardian section
    const parentName = form.querySelector('input[name="parentName"]')?.value || '';
    const parentContact = form.querySelector('input[name="parentContact"]')?.value || '';

    // Academic section
    const schoolCollegeName = form.querySelector('input[name="schoolCollegeName"]')?.value || '';
    const studentClass = form.querySelector('input[name="studentClass"]')?.value || '';
    const registrationId = form.querySelector('input[name="registrationId"]')?.value || '';
    const preferredExamDate = form.querySelector('input[name="preferredExamDate"]')?.value || '';
    const subjects = form.querySelector('select[name="subjects"]')?.value || '';

    // Validation: Email and Contact Number are required
    if (!email || !email.trim()) {
        alert('❌ Email Address is required');
        return;
    }

    // Validation: Contact Number is required for UID generation
    if (!contactNumber || contactNumber.length < 5) {
        alert('❌ Contact Number is required and must have at least 5 digits');
        return;
    }

    // Append all data to FormData
    formData.append('fullName', fullName);
    if (dob) formData.append('dob', dob);
    if (gender) formData.append('gender', gender);
    if (email) formData.append('email', email);
    formData.append('contactNumber', contactNumber); // Required
    if (address) formData.append('address', address);
    if (state) formData.append('state', state);
    if (city) formData.append('city', city);
    if (parentName) formData.append('parentName', parentName);
    if (parentContact) formData.append('parentContact', parentContact);
    if (schoolCollegeName) formData.append('schoolCollegeName', schoolCollegeName);
    if (studentClass) formData.append('studentClass', studentClass);
    if (registrationId) formData.append('registrationId', registrationId);
    if (preferredExamDate) formData.append('preferredExamDate', preferredExamDate);
    if (subjects) formData.append('subjects', subjects);

    // Append photo if selected
    if (photoInput.files[0]) {
        formData.append('photo', photoInput.files[0]);
    }

    // Show loading state
    const submitBtn = form.querySelector('button[type="submit"]');
    const originalBtnContent = submitBtn.innerHTML;
    submitBtn.disabled = true;
    submitBtn.innerHTML = `
        <div class="flex items-center gap-2">
            <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
            <span>Submitting...</span>
        </div>
    `;

    try {
        const response = await fetch(`${BASE_URL}/admin/student/register`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            const responseData = await response.json();
            const studentUid = responseData.uid || '';
            alert('✅ ' + (responseData.message || 'Student registered successfully'));
            // Redirect to payment page with name, phone, email, and studentUid as URL parameters
            const encodedName = encodeURIComponent(fullName);
            const encodedPhone = encodeURIComponent(contactNumber);
            const encodedEmail = encodeURIComponent(email);
            const encodedUid = encodeURIComponent(studentUid);
            window.location.href = `${BASE_URL}/payment?name=${encodedName}&phone=${encodedPhone}&email=${encodedEmail}&uid=${encodedUid}`;
        } else {
            const error = await response.text();
            alert('❌ Error: ' + error);
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalBtnContent;
        }
    } catch (error) {
        console.error('Error submitting form:', error);
        alert('❌ Failed to register student. Please check your connection and try again.');
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalBtnContent;
    }
});
