# MongoDB Setup and Explanation Guide

This guide provides comprehensive setup instructions and detailed technical explanations for the MongoDB portion of the **Gym Management System** (`gymdb`).

---

## Table of Contents
1. [Prerequisites & Installation](#1-prerequisites--installation)
   - [macOS](#macos)
   - [Ubuntu / Linux](#ubuntu--linux)
   - [Windows](#windows)
2. [Database and Collection Design](#2-database-and-collection-design)
   - [Schema Validation (`$jsonSchema`)](#schema-validation-jsonschema)
   - [Validation Field Details](#validation-field-details)
3. [CRUD Operations](#3-crud-operations)
   - [Insert Operations](#insert-operations)
   - [Update Operations](#update-operations)
   - [Replace Operations](#replace-operations)
   - [Query Operations & Logical Operators](#query-operations--logical-operators)
   - [Delete Operations](#delete-operations)
4. [Aggregation Pipeline](#4-aggregation-pipeline)
   - [Group & Sum](#group--sum)
   - [Group & Min/Max](#group--minmax)
5. [Running the Scripts](#5-running-the-scripts)

---

## 1. Prerequisites & Installation

To run the MongoDB exercises, you need the **MongoDB Community Server** and the **MongoDB Shell (`mongosh`)** installed on your system.

### macOS
1. Install the MongoDB Homebrew tap and the community formula:
   ```bash
   brew tap mongodb/brew
   brew install mongodb-community
   ```
2. Start the MongoDB service:
   ```bash
   brew services start mongodb-community
   ```
3. To install the shell separately if needed:
   ```bash
   brew install mongosh
   ```

### Ubuntu / Linux
1. Import the MongoDB public GPG Key:
   ```bash
   curl -fsSL https://www.mongodb.org/static/pgp/server-6.0.asc | \
     sudo gpg --oformalize --dearmor -o /usr/share/keyrings/mongodb-server-6.0.gpg
   ```
2. Create the list file for Ubuntu (e.g., Ubuntu 22.04 Jammy):
   ```bash
   echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-6.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/6.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-6.0.list
   ```
3. Reload local package database and install MongoDB:
   ```bash
   sudo apt-get update
   sudo apt-get install -y mongodb-org
   ```
4. Start the service:
   ```bash
   sudo systemctl daemon-reload
   sudo systemctl start mongod
   sudo systemctl enable mongod
   ```

### Windows
1. Download the MongoDB Community `.msi` installer from the [MongoDB Download Center](https://www.mongodb.com/try/download/community).
2. Follow the setup wizard, ensuring "Install MongoDB as a Service" is checked.
3. Download and install the **MongoDB Shell (`mongosh`)** from [MongoDB Shell Download](https://www.mongodb.com/try/download/shell).
4. Start the service using Administrative PowerShell:
   ```powershell
   Start-Service MongoDB
   ```

---

## 2. Database and Collection Design

The domain is a **Gym Management System** tracking gym members. We store documents in the `MEMBER` collection of the `gymdb` database.

### Schema Validation (`$jsonSchema`)

MongoDB supports document validation using JSON Schema, allowing us to enforce a rigid schema on top of MongoDB's flexible, schemaless nature. This ensures that every member document contains proper types and constraint ranges.

The collection is created with the following schema:

```javascript
db.createCollection("MEMBER", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["member_id", "member_name", "gender", "age", "body_type"],
      properties: {
        member_id: { bsonType: "int" },
        branch_id: { bsonType: "int" },
        member_name: { bsonType: "string" },
        gender: { bsonType: "string", enum: ["Male", "Female", "Other"] },
        age: { bsonType: "int", minimum: 1 },
        body_type: { bsonType: "string", enum: ["Mesoderm", "Endoderm", "Ectoderm"] },
        weight: { bsonType: "double" },
        date_of_join: { bsonType: "date" },
        address_city: { bsonType: "string" },
        address_street: { bsonType: "string" },
        address_pincode: { bsonType: "string" }
      }
    }
  }
});
```

### Validation Field Details

* **`member_id`**: An integer identifying the member. Required.
* **`branch_id`**: An integer representing the branch location. Optional.
* **`member_name`**: The member's full name. Required string.
* **`gender`**: Strictly constrained to either `"Male"`, `"Female"`, or `"Other"`. Required.
* **`age`**: An integer which must be at least `1`. Required.
* **`body_type`**: A string restricted to the three somatic body types: `"Mesoderm"`, `"Endoderm"`, or `"Ectoderm"`. Required.
* **`weight`**: The double-precision representation of member weight in kilograms. Optional.
* **`date_of_join`**: ISODate format of when the member joined. Optional.
* **`address_city`, `address_street`, `address_pincode`**: Address detail fields. Optional strings.

---

## 3. CRUD Operations

### Insert Operations

#### Inserting a Single Document (`insertOne`)
To add a single member, use `insertOne()`. We pass numeric fields properly as integers (e.g., `1062`) and doubles using `Double()`.

```javascript
db.MEMBER.insertOne({
  member_id: 1062,
  branch_id: 102,
  member_name: "Amaz",
  gender: "Male",
  age: 28,
  body_type: "Mesoderm",
  weight: Double(80),
  date_of_join: new Date(),
  address_city: "MADURAI",
  address_street: "POWER HOUSE GYM",
  address_pincode: "625001"
});
```

#### Inserting Multiple Documents (`insertMany`)
To add several members at once, we supply an array of documents to `insertMany()`:

```javascript
db.MEMBER.insertMany([
  {
    member_id: 1063,
    branch_id: 103,
    member_name: "Praveena",
    gender: "Female",
    age: 24,
    body_type: "Ectoderm",
    weight: Double(55),
    date_of_join: new Date(),
    address_city: "CHENNAI",
    address_street: "FITNESS WORLD",
    address_pincode: "600028"
  },
  {
    member_id: 1064,
    branch_id: 104,
    member_name: "Krish",
    gender: "Male",
    age: 30,
    body_type: "Endoderm",
    weight: Double(90),
    date_of_join: new Date(),
    address_city: "COIMBATORE",
    address_street: "IRON TEMPLE GYM",
    address_pincode: "641001"
  }
]);
```

---

### Update Operations

#### Incremental Changes (`$inc`)
You can use the `$inc` operator to increment or decrement numeric values without replacing the entire document.
```javascript
// Increase weight by 5 for member 1062
db.MEMBER.updateOne(
  { member_id: 1062 },
  { $inc: { weight: Double(5) } }
);
```

#### Modifying Fields (`$set`)
The `$set` operator updates the value of the specified field (or adds it if not present).
```javascript
db.MEMBER.updateOne(
  { member_id: 1062 },
  { $set: { body_type: "Endoderm" } }
);
```

#### Renaming Fields (`$rename`)
The `$rename` operator updates the key name of a field.
```javascript
db.MEMBER.updateOne(
  { member_id: 1062 },
  { $rename: { "joining_date": "date_of_join" } }
);
```

#### Bulk Updates (`updateMany`)
To update all documents matching a certain filter (or empty filter `{}` for all documents):
```javascript
// Increase weight by 5 for all members
db.MEMBER.updateMany(
  {},
  { $inc: { weight: Double(5) } }
);
```

---

### Replace Operations

#### Replacing a Document (`replaceOne`)
Unlike `updateOne` which changes specific fields, `replaceOne` replaces the entire matching document content (excluding the unique `_id`). This is useful for restructuring or full overwrites.

```javascript
db.MEMBER.replaceOne(
  { member_id: 1062 },
  {
    member_id: 1062,
    branch_id: 102,
    member_name: "Amosh",
    gender: "Male",
    age: 28,
    body_type: "Mesoderm",
    weight: Double(85),
    date_of_join: new Date(),
    address_city: "MADURAI",
    address_street: "POWER HOUSE GYM",
    address_pincode: "625001"
  }
);
```

---

### Query Operations & Logical Operators

#### Retrieve All Documents
```javascript
db.MEMBER.find();
```

#### Find by Specific Attribute
```javascript
db.MEMBER.find({ member_id: 1062 });
```

#### Logical OR (`$or`)
The `$or` operator evaluates an array of expression clauses and returns documents satisfying at least one clause.
```javascript
// Find members who are either Male OR have Ectoderm body type
db.MEMBER.find({
  $or: [
    { gender: "Male" },
    { body_type: "Ectoderm" }
  ]
});
```

#### Range Queries (`$gt`, `$lt`)
Perform comparison queries on numeric and date fields:
- `$gt` (Greater than)
- `$lt` (Less than)

```javascript
// Find members with age greater than 25
db.MEMBER.find({ age: { $gt: 25 } });

// Find members with age less than 27
db.MEMBER.find({ age: { $lt: 27 } });
```

#### Inclusion / Exclusion (`$in`, `$nin`)
- `$in`: Matches any value specified in an array.
- `$nin`: Excludes any value specified in an array.

```javascript
// Matches body_type of Mesoderm or Endoderm
db.MEMBER.find({
  body_type: { $in: ["Mesoderm", "Endoderm"] }
});

// Matches body_type NOT of Mesoderm or Endoderm
db.MEMBER.find({
  body_type: { $nin: ["Mesoderm", "Endoderm"] }
});
```

#### Sorting Results (`sort`)
Pass an object specifying fields to sort, where `1` represents ascending order and `-1` represents descending.
```javascript
// Sort ascending by member name
db.MEMBER.find().sort({ member_name: 1 });

// Sort descending by member ID
db.MEMBER.find().sort({ member_id: -1 });
```

---

### Delete Operations

#### Delete a Single Document (`deleteOne`)
Removes the first document matching the query.
```javascript
db.MEMBER.deleteOne({ member_id: 1062 });
```

#### Delete Multiple Documents (`deleteMany`)
Removes all documents matching the criteria.
```javascript
// Delete all members under 24 years of age
db.MEMBER.deleteMany({ age: { $lt: 24 } });

// Clear the collection entirely
db.MEMBER.deleteMany({});
```

---

## 4. Aggregation Pipeline

MongoDB's Aggregation Pipeline framework is used to process documents and return computed results.

### Group & Sum
We can group documents by a field and compute sum counts. This acts like SQL's `GROUP BY` and `COUNT()`.

```javascript
// Count total members per body type
db.MEMBER.aggregate([
  {
    $group: {
      _id: "$body_type",
      total_members: { $sum: 1 }
    }
  }
]);
```
**Sample Output:**
```json
[
  { "_id": "Ectoderm", "total_members": 2 },
  { "_id": "Mesoderm", "total_members": 2 },
  { "_id": "Endoderm", "total_members": 1 }
]
```

### Group & Min/Max
This query groups members by gender and computes the total count as well as minimum and maximum weights in each group.

```javascript
db.MEMBER.aggregate([
  {
    $group: {
      _id: "$gender",
      total: { $sum: 1 },
      max_weight: { $max: "$weight" },
      min_weight: { $min: "$weight" }
    }
  }
]);
```
**Sample Output:**
```json
[
  { "_id": "Female", "total": 2, "max_weight": 66, "min_weight": 60 },
  { "_id": "Male", "total": 3, "max_weight": 95, "min_weight": 73 }
]
```

---

## 5. Running the Scripts

To run all operations sequentially on your local database instance:

1. **Enter the MongoDB Shell (`mongosh`)**:
   ```bash
   mongosh
   ```
2. **Switch database context**:
   ```javascript
   use gymdb
   ```
3. **Run script containing validation schema and operations**:
   Run the following from your terminal to stream commands directly to the shell:
   ```bash
   mongosh gymdb < MongoDB/"DDL(Data Definition Language).json"
   ```
