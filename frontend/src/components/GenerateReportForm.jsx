import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format, startOfDay, endOfDay } from "date-fns";

function GenerateReportModal({ show, onHide, onSubmit }) {
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);

    const handleGenerateReport = () => {
        if (!startDate || !endDate) {
            alert("Please enter both start and end dates.");
            return;
        }

        // Format the start date to start of the day (00:00) and end date to end of the day (23:59)
        const formattedStartDate = format(startOfDay(startDate), "yyyy-MM-dd HH:mm:ss");
        const formattedEndDate = format(endOfDay(endDate), "yyyy-MM-dd HH:mm:ss");

        onSubmit({ startDateTime: formattedStartDate, endDateTime: formattedEndDate });
        onHide();
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Generate Report</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="formStartDate">
                        <Form.Label>Start Date</Form.Label>
                        <DatePicker
                            placeholderText="Select start date"
                            selected={startDate}
                            onChange={(date) => setStartDate(date)}
                            dateFormat="yyyy-MM-dd"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formEndDate">
                        <Form.Label>End Date</Form.Label>
                        <DatePicker
                            placeholderText="Select end date"
                            selected={endDate}
                            onChange={(date) => setEndDate(date)}
                            dateFormat="yyyy-MM-dd"
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
