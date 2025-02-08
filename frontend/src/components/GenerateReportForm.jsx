import React, { useState } from "react";
import { Modal, Button, Form, Row, Col } from "react-bootstrap";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format, startOfDay, endOfDay } from "date-fns";

function GenerateReportModal({ show, onHide, onSubmit }) {
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [reportAlgorithm, setReportAlgorithm] = useState("EVERY_IN_OUT");
    const [fileFormat, setFileFormat] = useState("XLSX");
    const [sendToEmail, setSendToEmail] = useState('aaa');
    const reportAlgorithms = ["FIRST_IN_LAST_OUT", "EVERY_IN_OUT"];
    const fileFormats = ["XLS", "XLSX"]

    const handleGenerateReport = () => {
        if (!startDate || !endDate) {
            alert("Please enter both start and end dates.");
            return;
        }

        // Format the start date to start of the day (00:00) and end date to end of the day (23:59)
        const formattedStartDate = format(startOfDay(startDate), "yyyy-MM-dd HH:mm:ss");
        const formattedEndDate = format(endOfDay(endDate), "yyyy-MM-dd HH:mm:ss");

        onSubmit({ startDateTime: formattedStartDate, endDateTime: formattedEndDate,
            reportAlgorithm: reportAlgorithm, reportFileFormat: fileFormat,
            sendToEmail: sendToEmail
        });
        onHide();
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Генериране на справка</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Row>
                        <Col>
                            <Form.Group className="mb-3" controlId="formStartDate">
                                <Form.Label>Start Date</Form.Label>
                                <DatePicker
                                    placeholderText="Select start date"
                                    selected={startDate}
                                    onChange={(date) => setStartDate(date)}
                                    dateFormat="yyyy-MM-dd"
                                    className="form-control"

                                />
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group className="mb-3" controlId="formEndDate">
                                <Form.Label>End Date</Form.Label>
                                <div>
                                    <DatePicker
                                        placeholderText="Select end date"
                                        selected={endDate}
                                        onChange={(date) => setEndDate(date)}
                                        dateFormat="yyyy-MM-dd"
                                        className="form-control"
                                    />
                                </div>

                            </Form.Group>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form.Group controlId="formReportAlgorithm" className="">
                                <Form.Label>Алгоритъм на справките</Form.Label>
                                <Form.Select
                                    name="reportAlgorithm"
                                    value={reportAlgorithm}
                                    onChange={(algorithm) => setReportAlgorithm(algorithm.target.value)}
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
                            <Form.Group controlId="formReportFileFormat" className="">
                                <Form.Label>Файлов формат</Form.Label>
                                <Form.Select
                                    name="fileFormat"
                                    value={fileFormat}
                                    onChange={(format) => setFileFormat(format.target.value)}
                                >
                                    {fileFormats.map((format) => (
                                        <option key={format} value={format}>
                                            {format}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form.Group controlId="formSendToEmail" className="">
                                <Form.Label>Изпрати на</Form.Label>
                                <Form.Control
                                    type="email"
                                    name="sendToEmail"
                                    placeholder="Въведете електронна поща"
                                    value={sendToEmail}
                                    onChange={(e) => setSendToEmail(e.target.value)}
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
                <Button variant="primary" onClick={handleGenerateReport}>
                    Generate
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default GenerateReportModal;
