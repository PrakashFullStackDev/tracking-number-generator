# tracking-number-generator

# Request Payload

http://localhost:8080/api/tracking-number?originCountry=IN&destinationCountry=UK&weight=1.5&createdAt=2023-01-01T00:00:00Z&customerId=123e4567-e89b-12d3-a456-426614174000&customerName=Test%20Customer

# Response Payload

{
    "tracking_number": "TN-IN-UK-100003",
    "created_at": "2025-06-12T16:41:34.158255100Z"
}
