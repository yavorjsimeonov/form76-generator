import React from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import ReportList from "../../components/ReportList";
import LeftMenu from "../../components/common/LeftMenu";

function ReportsPage() {
    return (
        <div className="">
            <Container fluid>
                <Row>
                    <Col md={2} id="sidebar-wrapper">
                        <LeftMenu activeKey="reports"/>
                    </Col>
                    <Col md={10} id="page-content-wrapper" >
                        <Header/>
                        <div styleName="clear: both;"></div>
                        <Container fluid="md">
                            <Row className="justify-content-md-center">
                                <Col md={12}>
                                    <ReportList showAdminAndLocation={true} />
                                </Col>
                            </Row>
                        </Container>
                        <Footer />
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default ReportsPage;