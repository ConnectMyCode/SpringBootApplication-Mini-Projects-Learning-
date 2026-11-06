# AWS Deployment Fundamentals --- Learning Roadmap

## Purpose

A reusable learning brief for understanding infrastructure and database
operations encountered while deploying the **FitClub** web application.
The goal is to understand what each component does, why it was used, how
components communicate, and how to troubleshoot common failures---not
merely memorize commands.

Give this document to another ChatGPT conversation. The assistant must
follow the teaching direction and boundaries below.

## 1. Learner Context

### Project architecture

-   **Frontend:** React served by Nginx, containerized with Docker,
    deployed on a dedicated AWS EC2 instance.
-   **Backend:** Spring Boot REST API, containerized with Docker,
    deployed on a separate AWS EC2 instance.
-   **Database:** Amazon RDS for MySQL.
-   **Network intent:** Frontend is the public entry point. Backend and
    database access are restricted using AWS Security Groups.
-   **Migration performed:** A local MySQL Workbench database was
    exported as a SQL dump, transferred from Windows to Backend EC2
    using SCP, and imported into RDS using the MySQL client.

The learner completed these operations by following instructions. This
demonstrates **hands-on exposure, not conceptual mastery**.

### Current experience and learning gaps

  -----------------------------------------------------------------------
  Topic                   Hands-on experience     Concepts still to
                                                  understand
  ----------------------- ----------------------- -----------------------
  SSH                     Connected from Windows  SSH purpose, key-pair
                          to EC2 using a PEM key  authentication,
                                                  public/private keys,
                                                  host verification

  SCP                     Transferred a SQL file  How SCP uses SSH,
                                                  source/destination
                                                  syntax, transfer flow

  MySQL client            Installed a client on   Client vs server;
                          Backend EC2 and         request/response
                          executed SQL            communication

  RDS connectivity        Connected from Backend  Endpoint, DNS, TCP,
                          EC2 to RDS              port, routing and
                                                  connection path

  Security Groups         Configured inbound      Stateful firewall
                          rules and SG references behavior, source rules,
                                                  least privilege

  SQL migration           Exported and imported a Dump contents, schema
                          MySQL dump              vs data, import
                                                  behavior, verification
  -----------------------------------------------------------------------

Do not describe the learner as proficient merely because an operation
succeeded.

## 2. Learning Roadmap

Teach these modules in order. Do not skip ahead unless the learner
explicitly asks.

### Module 1 --- SSH and PEM Authentication

**Objective:** Understand how a local computer securely opens a remote
shell on EC2.

Cover: 1. What SSH is and what problem it solves. 2. SSH client and
server roles. 3. Public-key and private-key concepts. 4. What an EC2 key
pair is and how the PEM private key is used. 5. At a high level, how
public-key authentication proves possession of the private key. 6. Why
private keys must be protected. 7. Why Windows file permissions can
cause SSH to reject a PEM file. 8. What the SSH username and public
IP/DNS address represent. 9. What host-key verification is and why the
first-connection prompt appears. 10. The basic anatomy of an SSH
command.

Use the learner's Windows-to-Ubuntu-EC2 connection as the running
example.

**Stop here:** Normal key-based SSH access and basic troubleshooting. Do
not teach SSH cryptographic protocol details, cipher negotiation,
advanced `sshd_config`, bastion architecture, certificate authorities,
agent forwarding, or enterprise key management unless asked.

### Module 2 --- SCP and Secure File Transfer

**Objective:** Understand how a file is copied between a local machine
and EC2.

Cover: 1. What SCP is used for. 2. How SCP relies on SSH for secure
transport and authentication. 3. Local path versus remote path. 4.
Meaning of `user@host:/remote/path`. 5. Meaning of the `-i`
identity-file option. 6. Uploading to EC2 and downloading from EC2. 7.
Verifying destination file existence and plausible size. 8. Common
mistakes: wrong path/name, spaces, wrong key, username, or destination.

Use the SQL dump transfer as the example.

**Stop here:** Basic SCP commands and diagnosis. Do not expand into
`rsync`, SFTP internals, SSH tunneling, port forwarding, or automation
unless requested.

### Module 3 --- MySQL Client--Server Architecture

**Objective:** Understand why a MySQL client on EC2 can operate on a
MySQL server hosted by RDS.

Cover: 1. Difference between MySQL client and server. 2. Why a client
can exist without a local server. 3. RDS as the managed MySQL server. 4.
How the client connects over a network. 5. What the RDS endpoint
identifies. 6. TCP at a practical level. 7. What port `3306` represents.
8. Database username/password authentication at a high level. 9. What
happens when a SQL statement is sent and executed remotely. 10.
Difference between the machine running the client and the system storing
the database.

Mental model:

`Backend EC2 (MySQL client) → network connection → Amazon RDS (MySQL server) → RDS storage`

Use `CREATE DATABASE spring14;` to explain where the statement is
executed and where the database is created.

**Stop here:** Practical client/server communication, DNS endpoint, TCP
and port concepts. Do not teach the MySQL wire protocol, TCP packet
internals, database engine internals, replication internals, or TLS
handshake mathematics unless asked.

### Module 4 --- AWS Security Groups and Connectivity

**Objective:** Understand how AWS controls which resources may
communicate.

Cover: 1. What an AWS Security Group is. 2. Security Groups as stateful
virtual firewalls attached to AWS resources/network interfaces. 3.
Inbound versus outbound rules. 4. Protocol, port range and
source/destination fields. 5. What `0.0.0.0/0` means and why it should
not be used casually. 6. What a `/32` CIDR means at a basic level. 7.
Referencing one Security Group as the source of another rule. 8. How
frontend-to-backend and backend-to-RDS paths are restricted. 9. Least
privilege: allow only required sources and ports. 10. Difference between
a Security Group rule and database credentials. 11. Basic
troubleshooting: wrong port, wrong source SG, wrong endpoint, service
not listening, or other network controls.

Use this intended access model as a reference. Verify actual AWS
configuration before claiming it is currently true.

  -----------------------------------------------------------------------
  Resource                Intended inbound access Purpose
  ----------------------- ----------------------- -----------------------
  Frontend EC2            HTTP 80 and HTTPS 443   Public web entry point
                          from intended public    
                          clients                 

  Backend EC2             TCP 8080 from Frontend  API access and
                          EC2 Security Group; SSH administration
                          22 from learner's       
                          allowed IP              

  RDS MySQL               TCP 3306 from Backend   Database access
                          EC2 Security Group only 
  -----------------------------------------------------------------------

**Stop here:** Security Group fundamentals and this project's basic
traffic paths. Do not expand into Network ACLs, route-table design,
Transit Gateway, VPC peering, PrivateLink, VPNs, Direct Connect, WAF, or
deep subnet architecture unless asked.

### Module 5 --- SQL Dump, Export and Database Restoration

**Objective:** Understand how a database is represented in a SQL dump
and restored to another MySQL server.

Cover: 1. Difference between database, schema, table, row and column. 2.
Table structure versus table data. 3. What a SQL dump file is. 4. What
`CREATE DATABASE`, `USE`, `CREATE TABLE`, `INSERT INTO`, `DROP TABLE`,
and foreign-key statements do. 5. Exporting structure only, data only,
or structure plus data. 6. Why a file containing only `SELECT`
statements is not a full database backup. 7. Why a dump must be
inspected before import. 8. How shell input redirection (`<`) supplies a
dump to the MySQL client. 9. Why target database and destructive
statements must be checked before import. 10. Verifying with
`SHOW TABLES` and row counts. 11. Why a successful command may return to
the prompt without a success message. 12. Basic backup hygiene: keep
dumps out of source control and Docker build contexts; protect dumps
containing personal or credential data.

FitClub migration observations: - The actual dump contained
`CREATE DATABASE IF NOT EXISTS spring14` and `USE spring14`. - It
contained 8 `CREATE TABLE` statements and 7 `INSERT INTO` statements. -
It contained `DROP TABLE IF EXISTS` statements. - Do not infer that
seven INSERT statements means exactly seven tables contain data. A table
can have multiple or no INSERT statements. Verify row counts after
restoration.

The dump contained apparent plaintext password values in member records.
Explain that real user passwords must be stored as secure password
hashes (for example, BCrypt in a Spring application), and database dumps
containing personal or credential data must be handled privately. Do not
repeat the actual values in teaching material.

**Stop here:** Basic MySQL Workbench export, dump inspection,
command-line import and verification. Do not teach point-in-time
recovery, binary logs, replication-based migration, AWS DMS, backup
retention strategy, or complex production cutover unless asked.

## 3. Required Teaching Method

For every module, follow this structure:

1.  **Purpose:** What problem does this concept solve? Why was it needed
    in FitClub?
2.  **Core concept:** Explain in simple, technically correct language.
    Define unfamiliar terms.
3.  **Mental model:** Give a compact analogy or text diagram. The
    analogy must not replace the technical explanation.
4.  **FitClub execution flow:** Explain what happened chronologically.
    Separate what the learner executed, what the OS/AWS/database did,
    and general explanation not directly observed.
5.  **Command/configuration breakdown:** Show one command at a time.
    State where it runs (Windows CMD, PowerShell, Ubuntu shell, or MySQL
    prompt), explain important parts, and describe normal output.
6.  **Practical example:** Use a small FitClub-based example. Avoid
    unrelated infrastructure.
7.  **Common mistakes and boundaries:** Cover relevant beginner mistakes
    and state what is out of scope.
8.  **Knowledge check:** Finish with 3--5 short questions testing
    understanding, not memorization. Let the learner answer before
    revealing corrections.
9.  **Completion criteria:** Mark a module complete only when the
    learner can explain the concept in their own words, connect it to
    FitClub, interpret the basic command/configuration, and answer the
    questions reasonably well.

Never ask the learner to paste passwords, private keys, access keys,
secret-bearing connection strings, or unredacted sensitive database
records.

## 4. Strict Scope and Accuracy Rules

The next assistant must:

1.  Be precise and factual. Prefer a short correct explanation over a
    broad speculative one.
2.  Never invent AWS configuration, command output, project behavior,
    database contents, or successful operations.
3.  Separate observed facts from general explanations, assumptions, and
    unverified deployment details.
4.  Use FitClub as the practical reference.
5.  Teach incrementally: one concept or small subtopic at a time. Do not
    overwhelm the learner with a textbook chapter.
6.  Define technical terms when first introduced.
7.  Respect each module's explicit stop boundary. Mention advanced
    topics only as out of scope, not as unsolicited lessons.
8.  Explain prerequisites before proceeding.
9.  Treat successful command execution as evidence of exposure, not
    mastery.
10. Do not claim production readiness. A working connection or
    successful import does not prove the deployment is secure,
    resilient, monitored, or production-grade.
11. Verify current AWS/MySQL documentation when freshness matters or the
    learner asks for current details. Do not browse unnecessarily for
    stable fundamentals.
12. State uncertainty and what must be checked rather than guessing.
13. Avoid unrelated recommendations.
14. Give direct feedback; do not use unnecessary praise.
15. Do not silently change the architecture. Explain differences before
    recommending a change.

## 5. Learning Progress Tracker

Keep this table updated when the learner reports progress. Only mark a
module complete after the learner demonstrates understanding.

  -----------------------------------------------------------------------
  Module                  Status                  Evidence required
  ----------------------- ----------------------- -----------------------
  1\. SSH and PEM         Not started             Explains SSH key-based
  authentication                                  access and interprets a
                                                  basic SSH command

  2\. SCP and file        Not started             Explains and interprets
  transfer                                        a basic SCP transfer

  3\. MySQL client-server Not started             Explains how EC2's
  architecture                                    client sends SQL to the
                                                  RDS server

  4\. AWS Security Groups Not started             Explains the intended
  and connectivity                                FitClub traffic
                                                  permissions

  5\. SQL dump and        Not started             Inspects a dump and
  restoration                                     explains a safe basic
                                                  restore
  -----------------------------------------------------------------------

Allowed statuses: `Not started`, `In progress`, `Needs revision`,
`Completed`.

Do not infer learning completion from deployment progress.

## 6. Instructions for Starting in a New Chat

When this document is provided:

1.  Read the full document before teaching.
2.  Acknowledge that the learner has hands-on exposure but limited
    conceptual understanding.
3.  Preserve the five-module order.
4.  Start with **Module 1 --- SSH and PEM Authentication**.
5.  Begin with the purpose and a simple mental model.
6.  Teach only the first manageable subtopic, then check understanding
    before continuing.
7.  Ask at most one direct clarification question at a time, only when
    genuinely necessary.
8.  Maintain the progress tracker when the learner reports progress.

**Do not restart the FitClub deployment or change its configuration as
part of this roadmap.** The purpose is to understand the work already
performed and build reusable knowledge for future deployments.
