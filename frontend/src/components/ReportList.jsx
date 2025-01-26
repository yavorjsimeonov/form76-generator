import React, { useState, useEffect } from "react";
import { Table, Button } from "react-bootstrap";
import { form76GeneratorApi } from "../api/Form76GeneratorApi";
import {useAuth} from "./common/AuthContext";

function ReportList({ locationId }) {
    const Auth = useAuth();
    const user = Auth.getUser();
    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchReports = async () => {
            try {
                const response = await form76GeneratorApi.getReportsForLocation(user, locationId);
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
        } catch (error) {
            console.error("Error sending file name to the backend:", error);
        }
    };

    if (loading) return <p>Loading reports...</p>;
    if (error) return <p>Error loading reports: {error}</p>;

    return (
        <div>
            <h3>Reports</h3>
            {reports.length > 0 ? (
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>File Name</th>
                        <th>Creation Date</th>
                        <th>Period Start</th>
                        <th>Period End</th>
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
                        </tr>
                    ))}
                    </tbody>
                </Table>
            ) : (
                <p>No reports available for this location.</p>
            )}
        </div>
    );
}

export default ReportList;
