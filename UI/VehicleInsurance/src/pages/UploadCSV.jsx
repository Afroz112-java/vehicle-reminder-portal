import React, { useState } from "react";
import axios from "axios";
import { toast } from "react-hot-toast";

export default function UploadCsv() {
  const [file, setFile] = useState(null);
  const [preview, setPreview] = useState([]);
  const [error, setError] = useState("");

  const requiredColumns = [
    "Full Name",
    "Email",
    "Phone",
    "Reg Number",
    "Brand",
    "Model",
    "InsuranceExpiryDate",
    "ServiceDueDate",
    "insurance_remainder_sent",
    "service_remainder_sent",
    "Vehicle_Type",
  ];

  // Convert date formats to dd-MM-yyyy
  const formatDate = (d) => {
    if (!d) return "";
    const dateObj = new Date(d);
    if (isNaN(dateObj)) return d;
    const dd = String(dateObj.getDate()).padStart(2, "0");
    const mm = String(dateObj.getMonth() + 1).padStart(2, "0");
    const yyyy = dateObj.getFullYear();
    return `${dd}-${mm}-${yyyy}`;
  };

  const validateColumns = (columns) => {
    return requiredColumns.every((col) => columns.includes(col));
  };

  const handleFileChange = (e) => {
    setError("");
    const selected = e.target.files[0];
    setFile(selected);

    if (!selected) return;

    const reader = new FileReader();

    reader.onload = (event) => {
      const csv = event.target.result;

      // Split rows
      const rows = csv.split("\n").map((r) => r.trim());
      const headers = rows[0].split("\t");

      // Validate columns
      if (!validateColumns(headers)) {
        setError("❌ Invalid CSV format. Please use the required template.");
        return;
      }

      // Convert rows to objects
      const data = rows.slice(1).map((row) => {
        const values = row.split("\t");
        let obj = {};
        headers.forEach((h, i) => {
          obj[h] = values[i] || "";
        });

        // Fix dates
        obj["InsuranceExpiryDate"] = formatDate(obj["InsuranceExpiryDate"]);
        obj["ServiceDueDate"] = formatDate(obj["ServiceDueDate"]);

        return obj;
      });

      setPreview(data);
    };

    reader.readAsText(selected);
  };

  const handleUpload = async () => {
    if (!file) {
      setError("Please select a file!");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("file", file);

      await axios.post("http://localhost:8080/api/vehicle/upload-csv", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

     toast.success("CSV Uploaded Successfully");
    } catch (err) {
      console.error(err);
      setError("Upload failed. Backend returned error.");
          toast.error("CSV Upload Failed!");
    }
  };

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-4">Upload Vehicle CSV</h2>

      <input
        type="file"
        accept=".csv,.txt"
        className="border p-2 rounded"
        onChange={handleFileChange}
      />

      {error && <p className="text-red-600 mt-2">{error}</p>}

      {preview.length > 0 && (
        <div className="mt-5">
          <h3 className="font-semibold">Preview (First 5 Rows):</h3>
          <table className="border mt-2">
            <thead>
              <tr>
                {requiredColumns.map((col) => (
                  <th key={col} className="border px-2 py-1">{col}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {preview.slice(0, 5).map((row, index) => (
                <tr key={index}>
                  {requiredColumns.map((col) => (
                    <td key={col} className="border px-2 py-1">{row[col]}</td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>

          <button
            onClick={handleUpload}
            className="mt-4 bg-blue-600 text-white px-4 py-2 rounded"
          >
            Upload CSV
          </button>
        </div>
      )}
    </div>
  );
}
