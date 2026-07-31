# MongoDB Operations and Aggregation Guide

This guide provides a comprehensive list of CRUD operations, advanced filters, and aggregation pipeline examples used on the `MEMBER` collection in the `gymdb` database.

---

## 1. Document Creation (CRUD - Create)

MongoDB offers flexibility in document insertions.

### Insert One Document
Use `insertOne()` to add a single gym member to the collection:
```javascript
db.MEMBER.insertOne({
  member_id: 1,
  member_name: "John Doe",
  gender: "Male",
  age: 28,
  body_type: "Mesoderm",
  weight: 75.2,
  date_of_join: ISODate("2023-05-10T00:00:00Z"),
  address_city: "New York"
});
```

### Insert Multiple Documents
Use `insertMany()` to batch insert multiple members:
```javascript
db.MEMBER.insertMany([
  {
    member_id: 2,
    member_name: "Jane Smith",
    gender: "Female",
    age: 24,
    body_type: "Ectoderm",
    weight: 58.0
  },
  {
    member_id: 3,
    member_name: "Sam Wilson",
    gender: "Other",
    age: 30,
    body_type: "Endoderm",
    weight: 90.4
  }
]);
```

---

## 2. Document Querying (CRUD - Read)

Querying in MongoDB uses filter documents to retrieve matches.

### Find All Documents
```javascript
db.MEMBER.find().pretty();
```

### Equality Filter
Find members belonging to a specific body type:
```javascript
db.MEMBER.find({ body_type: "Mesoderm" });
```

### Range Query (`$gt`, `$lt`)
Find members whose age is strictly greater than 25:
```javascript
db.MEMBER.find({ age: { $gt: 25 } });
```

### Logical OR Operator (`$or`)
Find members who are either over 30 or have a weight less than 60:
```javascript
db.MEMBER.find({
  $or: [
    { age: { $gt: 30 } },
    { weight: { $lt: 60.0 } }
  ]
});
```

### In-List Filter (`$in` and `$nin`)
Find members whose body type is either `Ectoderm` or `Endoderm`:
```javascript
db.MEMBER.find({ body_type: { $in: ["Ectoderm", "Endoderm"] } });
```

### Sorting Results
Sort members by weight descending (`-1` for descending, `1` for ascending):
```javascript
db.MEMBER.find().sort({ weight: -1 });
```

---

## 3. Document Updating (CRUD - Update)

MongoDB uses update operators to modify specific fields without rewriting the entire document.

### Update One Document with `$set` and `$inc`
Increase weight by `5` and modify city address for a member:
```javascript
db.MEMBER.updateOne(
  { member_id: 1 },
  {
    $inc: { weight: 5 },
    $set: { address_city: "Brooklyn" }
  }
);
```

### Field Renaming (`$rename`)
Rename the field `address_street` to `street_name` for a member:
```javascript
db.MEMBER.updateOne(
  { member_id: 1 },
  { $rename: { "address_street": "street_name" } }
);
```

### Complete Document Replacement (`replaceOne()`)
Replace a document entirely except for its internal `_id`:
```javascript
db.MEMBER.replaceOne(
  { member_id: 1 },
  {
    member_id: 1,
    member_name: "Johnathan Doe",
    gender: "Male",
    age: 29,
    body_type: "Mesoderm",
    weight: 80.2
  }
);
```

---

## 4. Document Deletion (CRUD - Delete)

### Delete One Document
Deletes the first document matching the query criteria:
```javascript
db.MEMBER.deleteOne({ member_id: 3 });
```

### Delete Multiple Documents
Deletes all documents matching the criteria:
```javascript
db.MEMBER.deleteMany({ address_city: "Chicago" });
```

---

## 5. Advanced Aggregation Pipelines

Aggregation pipelines process documents through multi-stage operations (filtering, grouping, computing averages, etc.).

### Group by Body Type and Count Members
Count the total number of members belonging to each body type:
```javascript
db.MEMBER.aggregate([
  {
    $group: {
      _id: "$body_type",
      total_members: { $sum: 1 }
    }
  }
]);
```

### Find Min and Max Weights Grouped by Gender
Find the minimum and maximum weights of members of each gender:
```javascript
db.MEMBER.aggregate([
  {
    $group: {
      _id: "$gender",
      min_weight: { $min: "$weight" },
      max_weight: { $max: "$weight" }
    }
  }
]);
```
