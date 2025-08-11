# Teacher Timetable Management API

A simple Spring Boot application for managing teacher timetables with conflict validation and business rule enforcement.

## Features

- **CRUD Operations**: Create, Read, Update, Delete timetable entries
- **Conflict Validation**: Ensures no scheduling conflicts between teachers and classes
- **Business Rules**: Enforces timetable constraints and validation
- **RESTful API**: Clean, simple REST endpoints
- **H2 Database**: In-memory database for easy testing

## Business Rules

1. **No Double Booking**: A teacher cannot be assigned to two classes at the same time
2. **Single Teacher Per Slot**: Each class period can have only one teacher
3. **Valid Days**: Only Monday through Saturday are allowed
4. **Valid Periods**: Only periods 1-8 are allowed
5. **Grade Range**: Grades 1-12 are supported
6. **Section Format**: Single uppercase letter (A, B, C, etc.)

## API Endpoints

### Teachers

- `GET /api/teachers` - Get all teachers
- `GET /api/teachers/{id}` - Get teacher by ID
- `POST /api/teachers` - Create new teacher
- `PUT /api/teachers/{id}` - Update teacher
- `DELETE /api/teachers/{id}` - Delete teacher

### Timetable

- `GET /api/timetable` - Get all timetable entries
- `GET /api/timetable/{id}` - Get timetable entry by ID
- `GET /api/timetable/teacher/{teacherId}` - Get timetable for specific teacher
- `GET /api/timetable/grade/{grade}/section/{section}` - Get timetable for specific grade/section
- `POST /api/timetable` - Create new timetable entry
- `PUT /api/timetable/{id}` - Update timetable entry
- `DELETE /api/timetable/{id}` - Delete timetable entry

## Request/Response Format

### Create/Update Timetable Entry

```json
{
  "teacherId": 1,
  "grade": 6,
  "section": "A",
  "subject": "Mathematics",
  "classDay": "Monday",
  "period": 1
}
```

### API Response

```json
{
  "success": true,
  "message": "Timetable entry created successfully",
  "data": {
    "id": 1,
    "teacher": {
      "id": 1,
      "name": "Bijay Panda",
      "email": "bijayaprasana.job@gmail.com"
    },
    "grade": 6,
    "section": "A",
    "subject": "Mathematics",
    "classDay": "Monday",
    "period": 1
  }
}
```

## Test Scenarios

### Success Cases
1.  Assign teacher A to Monday, Period 1 for Grade 6A
2.  Edit existing timetable and reassign to a valid empty slot

### Failure Cases
1.  Assign another teacher to same Grade 6A, Period 1 on Monday (Slot already filled)
2.  Assign same teacher A to different grade but same time (Teacher conflict)
3.  Assign with invalid period number 10 (Out of bounds)
4.  Edit to a slot where teacher is already busy (Conflict)

## Running the Application

1. **Prerequisites**: Java 21, Maven
2. **Build**: `mvn clean install`
3. **Run**: `mvn spring-boot:run`
4. **Access**: http://localhost:8080
5. **H2 Console**: http://localhost:8080/h2-console

## Database Configuration

- **URL**: `jdbc:h2:mem:timetabledb`
- **Username**: (leave empty)
- **Password**: (leave empty)

## Sample Data

The application automatically loads sample data:
- 3 teachers (John Smith, Mary Johnson, David Wilson)
- 3 sample timetable entries for testing

## Validation Rules

- **Day**: Must be one of ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
- **Period**: Must be between 1-8
- **Grade**: Must be between 1-12
- **Section**: Must be a single uppercase letter
- **Teacher ID**: Must reference an existing teacher
- **No Conflicts**: Business rule validation at service layer

## Error Handling

All endpoints return consistent error responses with clear messages:
- Validation errors
- Business rule violations
- Resource not found
- Conflict situations

## Unit Tests

Comprehensive test coverage for:
- Service layer business logic
- Conflict validation
- CRUD operations
- Error scenarios

Run tests with: `mvn test` 