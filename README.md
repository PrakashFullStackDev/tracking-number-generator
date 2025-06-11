# tracking-number-generator

# Request Payload

http://localhost:8080/tracking-number?originCountry=US&destinationCountry=UK&weight=1.5&createdAt=2023-01-01T00:00:00Z&customerId=123e4567-e89b-12d3-a456-426614174000&customerName=Test%20Customer

# Response Payload

{
    "tracking_number": "TN-0001-577313",
    "created_at": "2025-06-11T15:59:37.315588300Z",
    "origin_country": "US",
    "destination_country": "UK",
    "weight": 1.5,
    "customer_id": "123e4567-e89b-12d3-a456-426614174000",
    "customer_name": "Test Customer"
}
