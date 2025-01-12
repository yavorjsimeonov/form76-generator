import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

function GenerateReportModal({ show, onHide, onSubmit }) {
    const [startDateTime, setStartDateTime] = useState("");
    const [endDateTime, setEndDateTime] = useState("");

    const handleGenerateReport = () => {
        if (!startDateTime || !endDateTime) {
            alert("Please enter both start and end date-time.");
            return;
        }
        onSubmit({ startDateTime, endDateTime });
        onHide();
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Generate Report</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="formStartDateTime">
                        <Form.Label>Start Date and Time</Form.Label>
                        <Form.Control
                            type="text"
                            value={startDateTime}
                            onChange={(e) => setStartDateTime(e.target.value)}
                            placeholder="yyyy-MM-dd HH:mm:ss"
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formEndDateTime">
                        <Form.Label>End Date and Time</Form.Label>
                        <Form.Control
                            type="text"
                            value={endDateTime}
                            onChange={(e) => setEndDateTime(e.target.value)}
                            placeholder="yyyy-MM-dd HH:mm:ss"
                            required
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleGenerateReport}>
                    Generate
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default GenerateReportModal;
