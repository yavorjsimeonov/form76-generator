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
                                <Form.Label>Location Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="name"
                                    placeholder="Enter location name"
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
                                <Form.Label>Community ID</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="extCommunityId"
                                    placeholder="Enter external community ID"
                                    value={formData.extCommunityId || ""}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formExtCommunityUuid" className="">
                                <Form.Label>Community UUID</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="extCommunityUuid"
                                    placeholder="Enter external community UUID"
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
                                <Form.Label>Representative Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="representativeName"
                                    placeholder="Enter representative name"
                                    value={formData.representativeName || ""}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="formRepresentativeEmail" className="">
                                <Form.Label>Representative Email</Form.Label>
                                <Form.Control
                                    type="email"
                                    name="representativeEmail"
                                    placeholder="Enter representative email"
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
                                <Form.Label>Report Algorithm</Form.Label>
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
                            <Form.Group controlId="formActive" className="">
                                <Form.Label>Status</Form.Label>
                                <Form.Check
                                    type="checkbox"
                                    name="active"
                                    label="Active"
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
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    {title === "Edit Location" ? "Save Changes" : "Create"}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default LocationForm;
