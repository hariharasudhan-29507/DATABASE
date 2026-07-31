# MongoDB Schema Validation Guide

This document details the JSON Schema validation rules used in MongoDB to enforce strict document schemas for the `MEMBER` collection in `gymdb`.

---

## Schema Validation in MongoDB

MongoDB provides schema validation capability using `$jsonSchema`. When validation is enabled, any insert or update operation is checked against the schema. If a document does not match, the operation is rejected.

```javascript
db.createCollection("MEMBER", {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: [ "member_id", "member_name", "gender", "age", "body_type" ],
         properties: {
            member_id: {
               bsonType: "int",
               description: "must be an integer and is required"
            },
            member_name: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            gender: {
               enum: [ "Male", "Female", "Other" ],
               description: "can only be one of the enum values and is required"
            },
            age: {
               bsonType: "int",
               minimum: 1,
               description: "must be an integer greater than or equal to 1 and is required"
            },
            body_type: {
               enum: [ "Mesoderm", "Endoderm", "Ectoderm" ],
               description: "can only be one of the enum values and is required"
            },
            branch_id: {
               bsonType: "int",
               description: "must be an integer"
            },
            weight: {
               bsonType: "double",
               description: "must be a double"
            },
            date_of_join: {
               bsonType: "date",
               description: "must be a date"
            },
            address_city: {
               bsonType: "string",
               description: "must be a string"
            },
            address_street: {
               bsonType: "string",
               description: "must be a string"
            },
            address_pincode: {
               bsonType: "string",
               description: "must be a string"
            }
         }
      }
   }
});
```

---

## Detailed Rules & Constraints

### 1. Mandatory Fields
The following fields must be present in every single document inserted into the `MEMBER` collection:
- `member_id`
- `member_name`
- `gender`
- `age`
- `body_type`

### 2. Enumerations (Enum Constraints)
To ensure data consistency across reports, certain attributes are bounded to specific values:
- **`gender`**: Must be either `"Male"`, `"Female"`, or `"Other"`. (Note the sentence-case starting capitals, unlike Oracle's uppercase).
- **`body_type`**: Must be either `"Mesoderm"`, `"Endoderm"`, or `"Ectoderm"`.

### 3. Range Verification
- **`age`**: The minimum allowed value is `1`. This is enforced via the `minimum: 1` constraint.

---

## Testing Validation Rules

### Successful Insert Example
```javascript
db.MEMBER.insertOne({
  member_id: 101,
  member_name: "Bruce Wayne",
  gender: "Male",
  age: 32,
  body_type: "Mesoderm",
  weight: 85.5,
  date_of_join: ISODate("2023-01-15T00:00:00Z")
});
```

### Failed Insert Example (Throws DocumentValidationFailure)
```javascript
// This fails because weight is passed as an integer (101) instead of a double, or age is missing/invalid, or gender is lowercase 'male'
db.MEMBER.insertOne({
  member_id: 102,
  member_name: "Clark Kent",
  gender: "male", // Fail: Capitalized 'Male' required
  age: 0,         // Fail: Must be >= 1
  body_type: "Mesoderm"
});
```
