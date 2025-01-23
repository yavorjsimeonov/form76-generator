import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { useAuth } from "./common/AuthContext";
import { form76GeneratorApi } from "../api/Form76GeneratorApi";

function UserForm({
                           show,
                           onHide,
                           onSubmit,
                           initialData,
                           title = "Create User",
                       }) {

    const Auth = useAuth();
    const user = Auth.getUser();
    const [formData, setFormData] = useState(initialData);
    const [administrations, setAdministrations] = useState([]);

    // Fetch administrations for the dropdown
    useEffect(() => {
        const fetchAdministrations = async () => {
            try {
                const response = await form76GeneratorApi.getAdministrations(user);
                setAdministrations(response.data);
            } catch (error) {
                console.error("Error fetching administrations:", error);
            }
        };

        fetchAdministrations();
    }, []);

    // useEffect(() => {
    //     setFormData(initialData);
    // }, [initialData]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    const handleSubmit = () => {
        onSubmit(formData);
        setFormData({ firstName: "", lastName: "", email: "", username: "", password: "", role: "USER", administration: "" });
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="formFirstName">
                        <Form.Label>First Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter first name"
                            value={formData.firstName}
                            onChange={(e) =>
                                setFormData({ ...formData, firstName: e.target.value })
                            }
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="formLastName" className="mt-3">
                        <Form.Label>Last Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter last name"
                            value={formData.lastName}
                            onChange={(e) =>
                                setFormData({ ...formData, lastName: e.target.value })
                            }
                            required
                        />
                    </Form.Group>
                    <Form.Group controlId="formEmail" className="mt-3">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Enter email"
                            value={formData.email}
                            onChange={(e) =>
                                setFormData({ ...formData, email: e.target.value })
                            }
                        />
                    </Form.Group>
                    <Form.Group controlId="formUsername" className="mt-3">
                        <Form.Label>Username</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter username"
                            value={formData.username}
                            onChange={(e) =>
                                setFormData({ ...formData, username: e.target.value })
                            }
                        />
                    </Form.Group>
                    <Form.Group controlId="formPassword" className="mt-3">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Enter password"
                            value={formData.password}
                            onChange={(e) =>
                                setFormData({ ...formData, password: e.target.value })
                            }
                        />
                    </Form.Group>
                    <Form.Group controlId="formRole" className="mt-3">
                        <Form.Label>Role</Form.Label>
                        <Form.Select
                            value={formData.role}
                            onChange={(e) =>
                                setFormData({ ...formData, role: e.target.value })
                            }
                            required
                        >
                            <option value="">Select Role</option>
                            <option value="USER">USER</option>
                            <option value="ADMIN">ADMIN</option>
                        </Form.Select>
                    </Form.Group>
                    <Form.Group controlId="formAdministration" className="mt-3">
                        <Form.Label>Administration</Form.Label>
                        <Form.Select
                            value={formData.administration}
                            onChange={(e) =>
                                setFormData({ ...formData, administration: e.target.value })
                            }
                        >
                            <option value="">Select Administration</option>
                            {administrations.map((admin) => (
                                <option key={admin.id} value={admin.id}>
                                    {admin.name}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    {title === "Edit User" ? "Save Changes" : "Create"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default UserForm;
