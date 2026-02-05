package com.git.Admin.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.git.Activity;
import com.git.Admin.Entity.Faculty;
import com.git.Admin.Service.FacultyService;

@RestController
@RequestMapping("/admin/faculty")
public class FacultyController {

    @Autowired
    FacultyService facultyService;

    // POST Register Faculty
    @PostMapping("/register")
    public ResponseEntity<String> registerFaculty(
            @ModelAttribute Faculty faculty,
            @RequestParam("document") MultipartFile document,
            Model model) {

        try {
            facultyService.registerFaculty(faculty, document);
            return ResponseEntity.ok("Faculty registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong");
        }
    }

    // GET All Faculties
    @GetMapping
    public List<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    // GET Faculty by Username
    @GetMapping("/{username}")
    public Faculty getFacultyByUsername(@PathVariable String username) {
        return facultyService.getFacultyByUsername(username);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteFaculty(@PathVariable String username) {

        facultyService.deleteFaculty(username);
        return ResponseEntity.ok("Faculty deleted successfully");
    }

    // PUT Update Faculty
    @PutMapping("/{username}")
    public ResponseEntity<Faculty> updateFaculty(
            @PathVariable String username,
            @ModelAttribute Faculty updatedFaculty) {

        try {
            Faculty updated = facultyService.updateFaculty(username, updatedFaculty);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // GET Faculty Profile Picture
    @GetMapping("/{username}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {

        try {
            byte[] photo = facultyService.getProfilePicture(username);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(photo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // POST Update Faculty Profile Picture
    @PostMapping("/{username}/profile-picture")
    public ResponseEntity<String> updateProfilePicture(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file) {

        try {
            facultyService.updateProfilePicture(username, file);
            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile picture");
        }
    }

    /* ACTIVITY STATUS */

    // GET Faculty Activity
    @GetMapping("/{username}/status")
    public Activity getStatus(@PathVariable String username) {
        return facultyService.getAccountStatus(username);
    }

    // ACTIVATE
    @PutMapping("/{username}/activate")
    public void activate(@PathVariable String username) {
        facultyService.updateAccountStatus(username, Activity.ACTIVE);
    }

    // DEACTIVATE
    @PutMapping("/{username}/deactivate")
    public void deactivate(@PathVariable String username) {
        facultyService.updateAccountStatus(username, Activity.INACTIVE);
    }

    // BLOCK
    @PutMapping("/{username}/block")
    public void block(@PathVariable String username) {
        facultyService.updateAccountStatus(username, Activity.BLOCKED);
    }

    // DELETE (Set status to DELETED)
    @PutMapping("/{username}/delete")
    public void delete(@PathVariable String username) {
        facultyService.updateAccountStatus(username, Activity.DELETED);
    }

    // COUNT ENDPOINTS

    // Get total faculty count
    @GetMapping("/count/total")
    public long getTotalFacultyCount() {
        return facultyService.getTotalFacultyCount();
    }

    // Get active faculty count
    @GetMapping("/count/active")
    public long getActiveFacultyCount() {
        return facultyService.getActiveFacultyCount();
    }
}
