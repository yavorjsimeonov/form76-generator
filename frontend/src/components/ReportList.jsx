import React, { useState, useEffect } from "react";
import { Table, Button } from "react-bootstrap";
import { form76GeneratorApi } from "../api/Form76GeneratorApi";
import { useAuth } from "./common/AuthContext";

function ReportList({ locationId = null, showAdminAndLocation = false }) {
    const Auth = useAuth();
    const user = Auth.getUser();
    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchReports = async () => {
            try {
                let response;
                if (locationId) {
                    response = await form76GeneratorApi.getReportsForLocation(user, locationId);
                } else {
                    response = await form76GeneratorApi.getAllReports(user);
                }
                setReports(response.data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchReports();
    }, [locationId]);

    const handleDownload = async (reportId, reportFileName) => {
        try {
            const response = await form76GeneratorApi.downloadReport(user, reportId);
            const blob = new Blob([response.data], { type: response.headers["Content-Type"] });
            let a = document.createElement('a');
            let url = URL.createObjectURL(blob);
            a.href = url;
            a.download = reportFileName;
            a.click();
            document.body.removeChild(a);
            console.log(`Report "${reportId}" download completed.`);
        } catch (error) {
            console.error("Error sending report download to the backend:", error);
        }
    };

    if (loading) return <p>Loading reports...</p>;
    if (error) return <p>Error loading reports: {error}</p>;

    return (
        <div className="mt-3">
            <h2>Справки</h2>
            {reports.length > 0 ? (
                <Table striped bordered hover >
                    <thead>
                    <tr>
                        <th>Име на файл</th>
                        <th>Дата на създаване</th>
                        <th>Начална дата</th>
                        <th>Крайна дата</th>
                        {showAdminAndLocation && <th>Име на администрация</th>}
                        {showAdminAndLocation && <th>Име на локация</th>}
                    </tr>
                    </thead>
                    <tbody>
                    {reports.map((report) => (
                        <tr key={report.id}>
                            <td>
                                <Button
                                    variant="link"
                                    onClick={() => handleDownload(report.id, report.fileName)}
                                >
                                    {report.fileName}
                                </Button>
                            </td>
                            <td>{new Date(report.creationDate).toLocaleString()}</td>
                            <td>{new Date(report.reportPeriodStartDateTime).toLocaleString()}</td>
                            <td>{new Date(report.reportPeriodEndDateTime).toLocaleString()}</td>
                            {showAdminAndLocation && <td>{report.administrationName || "N/A"}</td>}
                            {showAdminAndLocation && <td>{report.locationName || "N/A"}</td>}
                        </tr>
                    ))}
                    </tbody>
                </Table>
            ) : (
                <p>No reports available.</p>
            )}
        </div>
    );
}

export default ReportList;
