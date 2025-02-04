import React, { useState, useEffect } from "react";
import { Modal, Button, Form, Container, Row, Col } from "react-bootstrap";
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
        setFormData({ firstName: "", lastName: "", email: "", username: "", password: "", confirmPassword: "", role: "USER", administration: "" });
    };

    return (
        <Modal show={show} onHide={onHide} size={'lg'}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                       <Row>
                           <Col>
                           <Form.Group controlId="formFirstName">
                               <Form.Label>Собствено име</Form.Label>
                               <Form.Control
                                   type="text"
                                   placeholder="Въведете собствено име"
                                   value={formData.firstName}
                                   onChange={(e) =>
                                       setFormData({ ...formData, firstName: e.target.value })
                                   }
                                   required
                               />
                           </Form.Group>
                           </Col>
                           <Col>
                            <Form.Group controlId="formLastName" className="">
                                <Form.Label>Фамилия</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Въведете фамилия"
                                    value={formData.lastName}
                                    onChange={(e) =>
                                        setFormData({ ...formData, lastName: e.target.value })
                                    }
                                    required
                                />
                            </Form.Group>
                           </Col>
                        </Row>
                    <Row>
                        <Col>
                            <Form.Group controlId="formEmail" className="">
                                <Form.Label>Електронна поща</Form.Label>
                                <Form.Control
                                    type="email"
                                    placeholder="Въведете Електронна поща"
                                    value={formData.email}
                                    onChange={(e) =>
                                        setFormData({ ...formData, email: e.target.value })
                                    }
                                    required
                                />
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formUsername" className="">
                                <Form.Label>Потребителско име</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Въведете потребителско име"
                                    value={formData.username}
                                    onChange={(e) =>
                                        setFormData({ ...formData, username: e.target.value })
                                    }
                                    required
                                />
                            </Form.Group>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form.Group controlId="formRole" className="">
                                <Form.Label>Роля</Form.Label>
                                <Form.Select
                                    value={formData.role}
                                    onChange={(e) =>
                                        setFormData({ ...formData, role: e.target.value })
                                    }
                                    required
                                >
                                    <option value="">Изберете роля</option>
                                    <option value="USER">USER</option>
                                    <option value="ADMIN">ADMIN</option>
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formAdministration" className="" hidden={formData.role !== "USER" || title === "Редакция на потребител"}>
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
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form.Group controlId="formAdminStatus" className="">
                                <Form.Label>Status</Form.Label>
                                <Form.Check
                                    type="checkbox"
                                    label="Active"
                                    checked={formData.active}
                                    onChange={(e) =>
                                        setFormData({ ...formData, active: e.target.checked })
                                    }
                                />
                            </Form.Group>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form.Group controlId="formPassword" className="">
                                <Form.Label>Парола</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Въведете парола"
                                    value={formData.password}
                                    onChange={(e) =>
                                        setFormData({ ...formData, password: e.target.value })
                                    }
                                />
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formPassword" className="">
                                <Form.Label>Потвърждение на парола</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Въведете потвърждение на парола"
                                    value={formData.confirmPassword}
                                    onChange={(e) =>
                                        setFormData({ ...formData, confirmPassword: e.target.value })
                                    }
                                />
                            </Form.Group>
                        </Col>
                    </Row>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    {title === "Редакция на потребител" ? "Запази промените" : "Добави потребител"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default UserForm;
