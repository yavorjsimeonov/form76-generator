import React from "react";
import { Card, Col, Row, Button, Container } from "react-bootstrap";

function LocationDetailsCard({ location, onEdit, onGenerate, onBack }) {
    return (
        <Card className="card mt-5">
            <Card.Title className="mt-3">Детайли</Card.Title>
            <Card.Body>
                <Row>
                    <Col md={6}>
                        <b>Име: </b>{location.name}<br />
                        <b>Външно Id: </b>{location.extCommunityId}<br />
                        <b>Външно UUId: </b>{location.extCommunityUuid}<br />
                        <b>Алгоритъм на справката: </b>{location.reportAlgorithm}<br />
                    </Col>
                    <Col md={6}>
                        <b>Име на представител: </b>{location.representativeName}<br />
                        <b>Електронна поща: </b>{location.representativeEmail}<br />
                        <b>Изпращане на email: </b>{location.sendEmail ? "Да" : "Не"}<br />
                        <b>Статус: </b>{location.active ? "Active" : "Inactive"}<br />
                    </Col>
                </Row>
            </Card.Body>
            <Card.Body>
                <Container>
                    <Row>
                        <Col>
                            <Button variant="warning" onClick={onEdit} className="me-2">Редакция</Button>
                            <Button variant="primary" onClick={onGenerate} className="me-2">Генериране на справка</Button>
                            <Button variant="secondary" onClick={onBack}>Назад</Button>
                        </Col>
                    </Row>
                </Container>
            </Card.Body>
        </Card>
    );
}

export default LocationDetailsCard;
