# Mock Project 062025 Group 03

## Environment Configuration

This project uses environment variables for configuration. To set up your local environment:

1. Create a `.env` file in the project root by copying the example file:
   ```
   cp .env.example .env
   ```

2. Edit the `.env` file with your local settings:
   ```
   # Database Configuration
   DB_URL=jdbc:postgresql://localhost:5432/your_db_name
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   DB_DDL_AUTO=update
   SHOW_SQL=true

   # JWT Configuration
   JWT_SECRET=your_secure_jwt_secret_key
   JWT_EXPIRATION=86400000
   ```

3. The application will automatically use these environment variables.

Note: The `.env` file is ignored by Git to avoid conflicts between developers and to keep sensitive information secure. 