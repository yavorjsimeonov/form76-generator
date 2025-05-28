import React from "react";
import { Button, Card, Container, Row, Col } from "react-bootstrap";

function LocationActionButtons({ onEdit, onGenerate, onBack }) {
    return (
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
    );
}

export default LocationActionButtons;
