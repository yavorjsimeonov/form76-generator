import React from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import ReportList from "../../components/ReportList";

function ReportsPage() {
    return (
        <div className="">
            <Header/>
            <Menu activeKey="reports"/>
            <Container fluid="md" className="">
                <Row className="justify-content-md-center">
                    <Col md={12}>
                        <h2>All Reports</h2>
                        <ReportList showAdminAndLocation={true} />
                    </Col>
                </Row>
            </Container>
            <Footer />
        </div>
    );
}

export default ReportsPage;