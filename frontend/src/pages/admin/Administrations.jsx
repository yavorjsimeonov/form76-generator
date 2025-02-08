import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import { config } from "../../api/Constants";
import Header from "../../components/common/Header";
import Menu from "../../components/common/Menu";
import Footer from "../../components/common/Footer";
import AdministrationFormModal from "../../components/AdministrationForm";
import { useAuth } from "../../components/common/AuthContext";
import { form76GeneratorApi } from "../../api/Form76GeneratorApi";
import LeftMenu from "../../components/common/LeftMenu";

function AdministrationsPage() {
    const Auth = useAuth();
    const user = Auth.getUser();
    const [administrations, setAdministrations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const fetchAdministrations = async () => {
            try {
                const response = await form76GeneratorApi.getAdministrations(user);
                setAdministrations(response.data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAdministrations();
    }, []);

    const handleCreate = async (newAdministration) => {
        try {
            const response = await form76GeneratorApi.createAdministration(user, newAdministration);
            setAdministrations([...administrations, response.data]);
        } catch (error) {
            console.error("Error creating administration:", error);
        } finally {
            setShowModal(false);
        }
    };

    return (
        <div>
            <Container fluid>
                <Row>
                    <Col md={2} id="sidebar-wrapper">
                        <LeftMenu activeKey="administrations"/>
                    </Col>
                    <Col md={10} id="page-content-wrapper" >
                        <Header/>
                        <div styleName="clear: both;"></div>
                        <Container fluid="md">
                            <Row className="justify-content-md-center">
                                <Col md={12}>
                                    <h2>Администрации</h2>
                                    {/* Show the button only if the user is an ADMIN */}
                                    {user.role === "ADMIN" && (
                                        <Button
                                            variant="primary"
                                            onClick={() => setShowModal(true)}
                                            style={{ marginBottom: "20px" }}
                                        >
                                            Добавяне на администрация
                                        </Button>
                                    )}
                                    {loading ? (
                                        <p>Loading...</p>
                                    ) : error ? (
                                        <p>Error: {error}</p>
                                    ) : administrations.length > 0 ? (
                                        <Table striped bordered hover>
                                            <thead>
                                            <tr>
                                                <th>Име</th>
                                                <th>Статус</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {administrations.map((administration) => (
                                                <tr key={administration.id}>
                                                    <td className="linkColumn">
                                                        <Link to={`/administrations/${administration.id}`}>
                                                            {administration.name}
                                                        </Link>
                                                    </td>
                                                    <td>{administration.active ? "Active" : "Inactive"}</td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </Table>
                                    ) : (
                                        <p>No administrations found.</p>
                                    )}
                                </Col>
                            </Row>
                        </Container>
                        <Footer />
                    </Col>
                </Row>
            </Container>
            <AdministrationFormModal
                show={showModal}
                onHide={() => setShowModal(false)}
                onSubmit={handleCreate}
                initialData={{ name: "", active: true }}
                title="Create Administration"
            />
        </div>
    );
}

export default AdministrationsPage;
