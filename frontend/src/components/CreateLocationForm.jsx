import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

function CreateLocationForm({
                               show,
                               onHide,
                               onSubmit,
                               initialData,
                               title = "Create Location",
                           }) {
    const [formData, setFormData] = useState(initialData);

    const reportAlgorithms = ["FIRST_IN_LAST_OUT", "EVERY_IN_OUT"];

    const handleSubmit = () => {
        onSubmit(formData);
        setFormData({ name: "", extCommunityId: "", extCommunityUuid: "", representativeName: "", representativeEmail: "", reportAlgorithm: "EVERY_IN_OUT", active: true });
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="formLocationName">
                        <Form.Label>Location Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter location name"
                            value={formData.name}
                            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        />
                    </Form.Group>
                    <Form.Group controlId="formExtCommunityId" className="mt-3">
                        <Form.Label>Community ID</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter external community ID"
                            value={formData.extCommunityId}
                            onChange={(e) => setFormData({ ...formData, extCommunityId: e.target.value })}
                        />
                    </Form.Group>
                    <Form.Group controlId="formExtCommunityUuid" className="mt-3">
                        <Form.Label>Community UUID</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter external community UUID"
                            value={formData.extCommunityUuid}
                            onChange={(e) => setFormData({ ...formData, extCommunityUuid: e.target.value })}
                        />
                    </Form.Group>
                    <Form.Group controlId="formRepresentativeName" className="mt-3">
                        <Form.Label>Representative Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter representative name"
                            value={formData.representativeName}
                            onChange={(e) => setFormData({ ...formData, representativeName: e.target.value })}
                        />
                    </Form.Group>
                    <Form.Group controlId="formRepresentativeEmail" className="mt-3">
                        <Form.Label>Representative Email</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Enter representative email"
                            value={formData.representativeEmail}
                            onChange={(e) => setFormData({ ...formData, representativeEmail: e.target.value })}
                        />
                    </Form.Group>
                    <Form.Group controlId="formReportAlgorithm" className="mt-3">
                        <Form.Label>Report Algorithm</Form.Label>
                        <Form.Select
                            value={formData.reportAlgorithm}
                            onChange={(e) => setFormData({ ...formData, reportAlgorithm: e.target.value })}
                        >
                            {reportAlgorithms.map((algorithm) => (
                                <option key={algorithm} value={algorithm}>
                                    {algorithm}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>
                    <Form.Group controlId="formActive" className="mt-3">
                        <Form.Check
                            type="checkbox"
                            label="Active"
                            checked={formData.active}
                            onChange={(e) => setFormData({ ...formData, active: e.target.checked })}
                        />
                    </Form.Group>
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

export default CreateLocationForm;
