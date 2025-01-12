import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

function LocationForm({ show, onHide, onSubmit, initialData, title }) {
    const [formData, setFormData] = useState(initialData);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3" controlId="formName">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formActive">
                        <Form.Check
                            type="checkbox"
                            name="active"
                            label="Active"
                            checked={formData.active}
                            onChange={handleChange}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formCommunityId">
                        <Form.Label>Community ID</Form.Label>
                        <Form.Control
                            type="number"
                            name="extCommunityId"
                            value={formData.extCommunityId}
                            onChange={handleChange}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formCommunityUuid">
                        <Form.Label>Community UUID</Form.Label>
                        <Form.Control
                            type="text"
                            name="extCommunityUuid"
                            value={formData.extCommunityUuid}
                            onChange={handleChange}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formReportAlgorithm">
                        <Form.Label>Report Algorithm</Form.Label>
                        <Form.Control
                            type="text"
                            name="reportAlgorithm"
                            value={formData.reportAlgorithm}
                            onChange={handleChange}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formRepresentativeName">
                        <Form.Label>Representative Name</Form.Label>
                        <Form.Control
                            type="text"
                            name="representativeName"
                            value={formData.representativeName}
                            onChange={handleChange}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formRepresentativeEmail">
                        <Form.Label>Representative Email</Form.Label>
                        <Form.Control
                            type="email"
                            name="representativeEmail"
                            value={formData.representativeEmail}
                            onChange={handleChange}
                        />
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        Save
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
}

export default LocationForm;
