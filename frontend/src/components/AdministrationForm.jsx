import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

function AdministrationFormModal({
                                     show,
                                     onHide,
                                     onSubmit,
                                     initialData = { name: "", active: true },
                                     title = "Create Administration",
                                 }) {
    const [formData, setFormData] = useState(initialData);

    useEffect(() => {
        setFormData(initialData);
    }, [initialData]);

    const handleSubmit = () => {
        onSubmit(formData);
        setFormData({ name: "", active: true });
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="formAdminName">
                        <Form.Label>Име</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Име на администрация"
                            value={formData.name}
                            onChange={(e) =>
                                setFormData({ ...formData, name: e.target.value })
                            }
                        />
                    </Form.Group>
                    <Form.Group controlId="formAdminStatus" className="mt-3">
                        <Form.Label>Статус</Form.Label>
                        <Form.Check
                            type="checkbox"
                            label="Active"
                            checked={formData.active}
                            onChange={(e) =>
                                setFormData({ ...formData, active: e.target.checked })
                            }
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    {title === "Edit Administration" ? "Save Changes" : "Create"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default AdministrationFormModal;
