import React, { useState, useEffect } from "react";
import { Modal, Button, Form, Row, Col } from "react-bootstrap";

function LocationForm({ show, onHide, onSubmit, initialData, title }) {
    const [formData, setFormData] = useState(initialData);
    const reportAlgorithms = ["FIRST_IN_LAST_OUT", "EVERY_IN_OUT"];

    useEffect(() => {
        // Update form data when initialData changes (e.g., when switching from create to edit)
        setFormData(initialData);
    }, [initialData]);

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
        <Modal show={show} onHide={onHide} size="lg">
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    {/* First Row: Location Name */}
                    <Row>
                        <Col>
                            <Form.Group controlId="formLocationName">
                                <Form.Label>Име на локация</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="name"
                                    placeholder="Въведете име на локация"
                                    value={formData.name || ""}
                                    onChange={handleChange}
                                    required
                                />
                            </Form.Group>
                        </Col>
                    </Row>

                    {/* Second Row: Community ID and Community UUID */}
                    <Row>
                        <Col>
                            <Form.Group controlId="formExtCommunityId" className="">
                                <Form.Label>Външно ID</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="extCommunityId"
                                    placeholder="Въведете външно ID"
                                    value={formData.extCommunityId || ""}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formExtCommunityUuid" className="">
                                <Form.Label>Външно UUID</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="extCommunityUuid"
                                    placeholder="Въведете външно UUID"
                                    value={formData.extCommunityUuid || ""}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                    </Row>

                    {/* Third Row: Representative Name and Email */}
                    <Row>
                        <Col>
                            <Form.Group controlId="formRepresentativeName" className="">
                                <Form.Label>Име на представител</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="representativeName"
                                    placeholder="Въведете име на представител"
                                    value={formData.representativeName || ""}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formRepresentativeEmail" className="">
                                <Form.Label>Електронна поща на представител</Form.Label>
                                <Form.Control
                                    type="email"
                                    name="representativeEmail"
                                    placeholder="Въведете електронна поща на представител"
                                    value={formData.representativeEmail || ""}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                    </Row>

                    {/* Fourth Row: Report Algorithm and Active Status */}
                    <Row>
                        <Col>
                            <Form.Group controlId="formReportAlgorithm" className="">
                                <Form.Label>Алгоритъм на справките</Form.Label>
                                <Form.Select
                                    name="reportAlgorithm"
                                    value={formData.reportAlgorithm || "EVERY_IN_OUT"}
                                    onChange={handleChange}
                                >
                                    {reportAlgorithms.map((algorithm) => (
                                        <option key={algorithm} value={algorithm}>
                                            {algorithm}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formActive" className="" styleClass="float:left">
                                <Form.Label>Send Email</Form.Label>
                                <Form.Check
                                    type="checkbox"
                                    name="active"
                                    label="Active"
                                    checked={formData.active || false}
                                    onChange={handleChange}
                                />
                            </Form.Group>

                            <Form.Group controlId="formActive" className="">
                                <Form.Label>Статус</Form.Label>
                                <Form.Check
                                    type="checkbox"
                                    name="active"
                                    label="Активен"
                                    checked={formData.active || false}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                    </Row>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Откажи
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    {title === "Редакция на локация" ? "Запази промените" : "Добави локация"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default LocationForm;
