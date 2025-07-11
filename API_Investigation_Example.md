# POST /api/cases/{caseId}/investigations - API Demo

## Request Example

### cURL Command
```bash
curl -X POST "http://localhost:8080/api/cases/CASE123/investigations" \
  -H "Content-Type: multipart/form-data" \
  -F 'data={"type":"Text","analysist":"Red stained object with serial no. #2921"}' \
  -F "file=@evidence1.png" \
  -F "file=@evidence2.png"
```

### Form Data
- **data** (JSON string):
  ```json
  {
    "type": "Text", 
    "analysist": "Red stained object with serial no. #2921"
  }
  ```
- **file**: Multiple files (evidence1.png, evidence2.png)

## Response Example

### Success Response (200)
```json
{
    "code": 200,
    "message": "Success",
    "result": {
        "type": "Text",
        "analysist": "Red stained object with serial no. #2921",
        "files": [
            {
                "filename": "evidence1.png",
                "url": "https://yourdomain.com/uploads/1720714800000-evidence1.png"
            },
            {
                "filename": "evidence2.png", 
                "url": "https://yourdomain.com/uploads/1720714800001-evidence2.png"
            }
        ]
    }
}
```

### Error Responses

#### Bad Request (400)
```json
{
    "code": 400,
    "message": "Invalid JSON format in data parameter",
    "result": null
}
```

#### Not Found (404)
```json
{
    "code": 404,
    "message": "Case not found with ID: CASE123",
    "result": null
}
```

#### File Type Error (400)
```json
{
    "code": 400,
    "message": "File type not allowed: exe. Allowed types: [png, jpg, jpeg, gif, pdf, doc, docx, mp4, avi, mov]",
    "result": null
}
```

## Features
- ✅ Multipart/form-data support
- ✅ Multiple file uploads
- ✅ File type validation
- ✅ File size limit (50MB)
- ✅ Case existence validation
- ✅ Comprehensive error handling
- ✅ Unique filename generation
- ✅ Full URL generation for file access 