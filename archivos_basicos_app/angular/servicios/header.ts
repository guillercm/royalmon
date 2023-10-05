import { HttpHeaders } from "@angular/common/http";

export const token = "eyJ0eSDFDFSDFSDFDS2432424zI1NiJ9.eyJzdWIiOiJqb3lmZSJ9.SnH-gyafVlJwTYAANhaHtpan41bPw8N6GSQRdDU4h09cXkg61NN8c2nKNeo7HZ3UM6rPn8yN9fy-XlFe8dSCdgZsuYc_b0LBk90tKWVS7RMBX0kWJs5sStjHDGZOWQ5VNK_VmV3y2WVkBdK5qBEDHQyqVIxj7UphPrjfzumjoYMj5RttbCbn1mWJTnC1Me4TkIrEhM31LjjBs3F0ijITu9xeXVTjHJWjv89eTOVsOZZ4-rJiFqbmogmBX5G3ucKNzJet_rAnNid8T7Cm6UoHjnnD5ePxRngKgLWqk83u8mYIz2MW6SQrdrVHHBof38BOapM8IfWF8RoHmguhPrm-GA";

export const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`
});