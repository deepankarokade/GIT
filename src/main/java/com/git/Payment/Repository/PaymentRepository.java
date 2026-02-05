package com.git.Payment.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Payment.Entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
}
