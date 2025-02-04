import React, { useState } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";
import { useAuth } from "./AuthContext";

function ChangePasswordForm({ show, onHide }) {
    const Auth = useAuth();
    const user = Auth.getUser();
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setError("New passwords do not match.");
            return;
        }

        try {
            await form76GeneratorApi.changePassword(user, {
                currentPassword,
                newPassword,
            });
            setSuccess(true);
            setError("");
            setTimeout(() => {
                onHide();
                setSuccess(false);
                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");
            }, 2000);
        } catch (err) {
            setError("Error changing password. Please try again.");
        }
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Change Password</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {success && <Alert variant="success">Password changed successfully!</Alert>}
                {error && <Alert variant="danger">{error}</Alert>}
                <Form onSubmit={handleSubmit}>
                    <Form.Group controlId="currentPassword">
                        <Form.Label>Current Password</Form.Label>
                        <Form.Control
                            type="password"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="newPassword">
                        <Form.Label>New Password</Form.Label>
                        <Form.Control
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="confirmPassword">
                        <Form.Label>Confirm New Password</Form.Label>
                        <Form.Control
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </Form.Group>
                    <Button variant="primary" type="submit" className="mt-3">
                        Change Password
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
}

export default ChangePasswordForm;
