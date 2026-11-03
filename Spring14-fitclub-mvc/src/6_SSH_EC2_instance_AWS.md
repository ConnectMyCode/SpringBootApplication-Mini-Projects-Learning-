![10__Windows to AWS EC2 SSH Command Flow.png](../../spring14frontend/src/10__Windows%20to%20AWS%20EC2%20SSH%20Command%20Flow.png)

Let's understand this from the fundamentals. There are several concepts involved in the SSH connection we established, and they are related to each other:

1. Windows folders and file permissions.
2. `.pem` files and public/private key cryptography.
3. Users, groups and inherited permissions in Windows.
4. SSH protocol and how it connects to an EC2 instance.

I'll explain each concept and then connect them into one execution flow.

# 1. First, what is an EC2 instance?

[Deploy a Spring Boot Application using AWS Elastic Beanstalk | Raj Rajhans](https://images.openai.com/static-rsc-4/NgVYlADGSygbxjsxnx0L17nksYKIe7uuWliFPmQDH9ffFp1KO8AMxm15ckQr5oaQHwlsE0kHE5DYD358nuNKJHJdcdwcszcunKlT2hxI-vplyo9E06wZaKmpuveGJgijz7zB1JHkYy7uv7T5jas3saKUAclVPoOFNY8rYPfzvXc?purpose=inline)

[rajrajhans.com](https://rajrajhans.com/2021/01/deploy-spring-boot-aws-ec2-rds/?utm_source=chatgpt.com)

Amazon EC2 (Elastic Compute Cloud) is an AWS service that allows you to create virtual servers in the cloud.

Think of an EC2 instance as a computer that exists in AWS rather than physically on your desk.

For example, your deployment has two EC2 instances:

| Instance     | Responsibility                        |
| ------------ | ------------------------------------- |
| Frontend EC2 | Hosts your React frontend using Nginx |
| Backend EC2  | Runs your Spring Boot application     |

Each instance has resources such as:

- CPU
- RAM
- Storage
- Operating system (Ubuntu in your case)
- Network configuration
- Security groups

Your EC2 instance runs independently of your Windows computer.

Mental model:

Your Windows computer is the machine you use to access and manage the remote Ubuntu computer.

# 2. What is SSH?

SSH stands for Secure Shell.

It is a network protocol that allows you to securely access and control another computer remotely.

For example, you executed:

```
ssh -i "C:\Amazon_WorkSPace\Spring-14-Fitclub_pem-key-pair\Spring14-Fitclub-Frontend.pem" ubuntu@15.207.14.172
```

Let's break it down.

| Component       | Meaning                                  |
| --------------- | ---------------------------------------- |
| `ssh`           | Start an SSH connection                  |
| `-i`            | Specify the identity/private key file    |
| `.pem` path     | Location of your private key             |
| `ubuntu`        | Username on the remote EC2 instance      |
| `15.207.14.172` | Public IPv4 address of your EC2 instance |

When the connection succeeds, you get access to the Ubuntu shell.

For example:

```
ubuntu@ip-172-31-xx-xx:~$
```

You are now executing commands on the remote EC2 machine, not your Windows computer.

## Why is SSH called Secure Shell?

It provides:

- Encrypted communication between your computer and the remote server.
- Authentication to verify that you are allowed to connect.
- A remote command-line interface.

SSH normally uses TCP port `22`.

## How does AWS Security Group relate to SSH?

Your EC2 security group acts like a virtual firewall.

[Connect EC2 using SSH Client. In my earlier story, I explained how we… | by Venkatgiri Sasanapuri | Medium](https://images.openai.com/static-rsc-4/HilWFdr_jTtq7NNtUKkeZZTeZAQvS62vJzvMCai5Vy5RoD0mCem4fI7tg5Ondey-XCVF5fPbeI7A5JREQ-fdcZB6DlbrZEiLNXI5O-2jcqleaAmdNo4_0MKw6Pi2htRzpKc1fpgHz-1OPz9Jcatht6uxYqWtXIw4NQmEiYkSnGI?purpose=inline)

[medium.com](https://medium.com/%40venkatagiri.sasanapuri/connect-ec2-using-ssh-client-dd60730caeb1?utm_source=chatgpt.com)

When you attempted SSH, your connection needed to pass through the EC2 security group's inbound rules.

You added:

| Type | Protocol | Port | Source |
| ---- | -------- | ---- | ------ |
| SSH  | TCP      | 22   | My IP  |

This means:

- Allow SSH connections.
- Use TCP protocol.
- Allow traffic through port 22.
- Accept connections from your current public IP.

Without this rule, the network connection can be blocked before SSH authentication even begins.

# 3. What is a `.pem` file?

A `.pem` file is a file format used to store cryptographic information, commonly keys and certificates.

In our EC2 deployment, your `.pem` file contains a private cryptographic key.

For example:

```
Spring14-Fitclub-Frontend.pem
```

You downloaded this key when creating or configuring your EC2 key pair.

But why does EC2 need it?

To understand that, we need to understand public-key cryptography.

## 3.1 Public key and private key

Public-key cryptography uses two mathematically related keys:

[Asymmetric and symmetric encryption:... | MindMesh Academy](https://images.openai.com/static-rsc-4/Hub8DInM7z9hsXtMg4CdXwiS_Qu1rGAhpbph2ij5B216pCUVunACtK2UFsNRwBXTUY3E24hwGif6jKYt7ktd7jw-jQqEO3EnItwmb1kmA0MCarmN8j_JBXteBvh217fOvGK3p7jwg8zSbxUD_SWEl33ajHmwLsp0M0cMzLnL7aE?purpose=inline)

[mindmeshacademy.com](https://www.mindmeshacademy.com/blog/articles/asymmetric-and-symmetric-encryption?utm_source=chatgpt.com)

| Public key                                        | Private key                     |
| ------------------------------------------------- | ------------------------------- |
| Can be shared                                     | Must remain secret              |
| Stored on the EC2 instance for SSH authentication | Stored on your Windows computer |
| Used to verify proof of private-key ownership     | Used to generate that proof     |

When you created the EC2 key pair:

1. AWS generated a public key and a private key.
2. AWS placed the public key in the EC2 instance's authorized SSH keys.
3. AWS provided you with the private key as a downloadable `.pem` file.
4. You stored that private key on your Windows computer.

Important: AWS does not need your `.pem` file to be uploaded every time you connect.

## 3.2 How does the `.pem` file allow you to connect?

Let's follow the authentication process.

[What Is an SSH Key? Authentication for Enterprise Infrastructure | TenG Spectrum Blog](https://images.openai.com/static-rsc-4/s8DbRa6tExQ4_r1cGh6HXg1cNYe1piiEfbVcM4WDf2jlMnBR3ZxWIyZ-KInIp1MyjXdwD8k0uLhpNXPie0c-kFii11zW73sG-U-CwhX3tPC14b1nJ7-qiJUYxrNtJxs12hIVok1AGaL1pPmoDbTEMrxvMvrtjIerXIthWXmq3N8?purpose=inline)

[tengspectrum.com](https://tengspectrum.com/blog/what-is-an-ssh-key/?utm_source=chatgpt.com)

1. Your Windows computer initiates an SSH connection.
2. The EC2 instance identifies the requested Linux user (`ubuntu`).
3. The SSH server checks the public keys authorized for that user.
4. Your SSH client uses your private key to prove possession of the corresponding key.
5. The server verifies the proof using the stored public key.
6. If authentication succeeds, the SSH server grants access.

The private key is not sent to EC2 as plain text during this authentication.

The important distinction: The `.pem` file proves your identity. The security group determines whether your network connection is allowed to reach the instance.

These solve two different problems.

# 4. What problem does a `.pem` file solve?

Imagine that your backend EC2 server is publicly reachable.

You don't want anyone who knows its public IP address to access its Linux terminal.

You need a way to verify that the person connecting is authorized.

There are different authentication mechanisms, including:

- Password-based authentication.
- Public-key authentication.

AWS EC2 commonly uses SSH public-key authentication.

With your `.pem` file, you can authenticate without entering the Ubuntu account's password.

It also avoids having to distribute a shared login password to everyone who needs access.

Security rule: Never share your private `.pem` file or commit it to GitHub.

# 5. What are file permissions in Windows?

Now let's understand the error you encountered:

```
WARNING: UNPROTECTED PRIVATE KEY FILE!

Permissions for 'Spring14-Fitclub-Frontend.pem'
are too open.
```

This was not an AWS error.

It was an error generated by your local SSH client on Windows.

Your SSH client checks whether other Windows users can access your private key.

Why?

Because if another user can read your private key, they might be able to use it to authenticate to your EC2 instance.

Windows provides a permission system to control who can access files and folders.

## 5.1 What is a parent folder?

Consider your actual directory structure:

C: Drive

Amazon_WorkSPace

Spring-14-Fitclub_pem-key-pair

FitClub-Backend.pem

Spring14-Fitclub-Frontend.pem

Here:

- `C:\` is the root directory.
- `Amazon_WorkSPace` is a folder.
- `Spring-14-Fitclub_pem-key-pair` is a subfolder.
- `Spring14-Fitclub-Frontend.pem` is a file inside that subfolder.

The folder containing the `.pem` file is its parent folder.

The folder above that is another parent directory.

## Why do parent folders matter?

Windows allows permissions to be inherited from parent folders.

For example:

Suppose you create a folder:

```
C:\Projects
```

You give the `Users` group permission to read files inside it.

Then you create:

```
C:\Projects\MyApplication\secret.pem
```

Unless inheritance is disabled or overridden, the file can inherit permissions from its parent folder.

This is called permission inheritance.

# 6. What are inherited permissions?

Windows uses Access Control Lists (ACLs) to determine who can access a file or folder and what operations they can perform.

An ACL contains entries called Access Control Entries (ACEs).

An entry specifies:

- Who gets access.
- What permissions they get.
- Whether access is allowed or denied.

For example:

| Principal      | Permission       |
| -------------- | ---------------- |
| Aryan          | Read             |
| Administrators | Full control     |
| Users          | Read and execute |

When a new file is created inside a folder, it generally inherits applicable permissions from its parent folder.

This avoids manually assigning permissions to every file.

## What does your command do?

You executed:

```
icacls "C:\Amazon_WorkSPace\Spring-14-Fitclub_pem-key-pair\Spring14-Fitclub-Frontend.pem" /inheritance:r
```

Let's break it down.

| Part             | Meaning                                                   |
| ---------------- | --------------------------------------------------------- |
| `icacls`         | Windows command for viewing and modifying ACL permissions |
| File path        | The file whose permissions you want to modify             |
| `/inheritance:r` | Disables inheritance and removes inherited ACEs           |

The `r` means remove inherited permissions, rather than simply disabling inheritance while copying them into explicit entries.

Why did we do this?

We wanted to stop broad permissions inherited from parent folders from being applied to your private key.

# 7. What are Users, BUILTIN\Users and Authenticated Users?

These are Windows security principals.

A security principal is an identity that Windows can use when applying permissions.

It can be:

- An individual user.
- A group of users.
- A built-in system account.

Let's understand the three names you encountered.

## 7.1 BUILTIN\Users

`BUILTIN\Users` is a predefined Windows local group.

It generally includes standard user accounts on the computer.

If a file grants this group read permission, members of that group can potentially read the file.

For your `.pem` file, this is undesirable because you don't want other standard users to read your private key.

## 7.2 Authenticated Users

`NT AUTHORITY\Authenticated Users` is a Windows security identity that represents users and accounts that have successfully authenticated to the computer or domain.

It is broader than just your individual Windows account.

Giving this group permission can make a file accessible to more authenticated accounts than intended.

## 7.3 BUILTIN\Administrators

This is another predefined Windows group.

It contains accounts with administrative privileges.

Administrators can perform privileged operations on the system, including managing files and permissions.

## 7.4 Why are these groups needed?

Imagine a computer shared by five employees.

Instead of assigning permissions to five people individually, an administrator can create a group and assign permissions to that group.

For example:

Example: Shared project folder

Project Folder

Read access granted to Developers group

User A

User B

User C

All three members receive the group's permissions.

This makes access management easier.

However, your private SSH key is different from a normal shared project file. It should have much more restrictive permissions.

# 8. What did we actually change in your `.pem` file?

This is an important question you asked:

> Is there something written inside the `.pem` that I gave read permission to the user?

No. We did not modify the contents of your `.pem` file.

We modified the Windows filesystem permissions associated with that file.

Think of it this way:

- The `.pem` file contains cryptographic key data.
- Windows ACL permissions control who can read or modify the file.
- Giving your Windows user read permission allows your SSH client to read the key.
- It does not change the cryptographic key itself.

Let's look at the commands.

Command 1 — Disable inherited permissions

```
icacls "Spring14-Fitclub-Frontend.pem" /inheritance:r
```

Stops inheriting permissions and removes inherited entries.

Command 2 — Remove broad group access

```
icacls "Spring14-Fitclub-Frontend.pem" /remove "BUILTIN\Users" "NT AUTHORITY\Authenticated Users"
```

Removes the specified group entries from the file's ACL.

Command 3 — Grant your Windows account read access

```
icacls "Spring14-Fitclub-Frontend.pem" /grant:r "%USERNAME%:R"
```

Grants read access to your current Windows account.

Command 4 — Verify permissions

```
icacls "Spring14-Fitclub-Frontend.pem"
```

Displays the current permissions.

The intended result is that your Windows user can read the private key, while broad user groups cannot.

Note: These commands modify the ACL on the key file itself. They do not automatically remove every possible permission from every other principal, such as an explicit Administrators entry. The goal is to restrict access sufficiently for SSH's private-key security check.

# 9. How everything works together

Now let's connect all the concepts.

[AWS Hands-On | SSH into Your EC2 Instance from Windows 10/11 — The Modern, No-Extra-Software Way | by Minoltan Issack | Jan, 2026 | Medium](https://images.openai.com/static-rsc-4/MXHB2Y7lJEmFb9HZqJbSGy1whIxjERFzAAA4FkNN6WRR0re_AJX4J1u3c_8iCp17MpXM4-P3_pB7x7sagWEtkNrM781z6N0wUKMY6mMlpajHZBCMcP9ZyoNzozS7SU5MbbwJ-BJ6hLJgT0ZO1rXvqYCWSAm4NTKgfVNwVZUQNto?purpose=inline)

[issackpaul95.medium.com](https://issackpaul95.medium.com/aws-hands-on-ssh-into-your-ec2-instance-from-windows-10-11-the-modern-no-extra-software-way-44bf8a1b7038?utm_source=chatgpt.com)

Imagine you want to connect to your Frontend EC2 instance.

Execution flow:

1. You execute the SSH command on Windows.
   ```
   ssh -i "Spring14-Fitclub-Frontend.pem" ubuntu@15.207.14.172
   ```
2. Network access is checked.

   Your EC2 security group must allow inbound SSH traffic on TCP port 22 from your IP.
3. Your local SSH client reads the `.pem` file.

   Windows permissions must allow your account to read it securely.
4. SSH authentication takes place.

   Your client proves possession of the private key corresponding to the public key authorized on EC2.
5. The EC2 instance verifies authentication.

   If authentication succeeds, the Ubuntu SSH server provides a shell for the `ubuntu` user.
6. You manage your remote server.

   You can execute commands, install Docker, transfer files and deploy your application.

# 10. Quick revision notes

| Concept               | Remember this                                           |
| --------------------- | ------------------------------------------------------- |
| EC2                   | Virtual server running in AWS                           |
| SSH                   | Secure protocol for remote server access                |
| SSH port              | TCP 22 by default                                       |
| `.pem`                | File containing your private cryptographic key          |
| Public key            | Stored on EC2 as an authorized key                      |
| Private key           | Kept securely on your local machine                     |
| Security Group        | AWS virtual firewall                                    |
| Parent folder         | Folder containing another folder or file                |
| ACL                   | Access Control List defining permissions                |
| Inheritance           | Permissions passed from parent folders to child objects |
| `BUILTIN\Users`       | Predefined Windows local group                          |
| `Authenticated Users` | Authenticated security identity                         |
| `icacls`              | Windows command for managing ACLs                       |

## Test your understanding

Since you are learning these concepts for the first time, try answering these five questions without looking at the explanations.

Knowledge check

0/5 answered

1\. What is the primary purpose of an EC2 instance?

To store private keys

To provide a virtual server in AWS

To encrypt Windows files

2\. Which key is stored on your local Windows machine?

Public key

Private key

Both keys must be uploaded to EC2

3\. What does /inheritance:r do in icacls?

Grants read permission

Removes inherited permission entries

Deletes the file

4\. Did granting your Windows user read permission change the contents of your .pem file?

Yes, it rewrote the private key

No, it changed filesystem permissions only

It generated a new public key

5\. What does the EC2 security group rule SSH TCP 22 control?

Which private key is valid

Whether network traffic can reach the SSH service

Which Windows user can read the .pem file

Check answers

The most important distinction to remember is this:

Security Group = Can the network connection reach EC2?

SSH + private key = Is the connecting client authorized to log in?

Windows ACL permissions = Is your local SSH client allowed to read the private key securely?

These are three separate security layers that work together.

![6_2_EC2 to RDS_ MySQL Database Flow.png](6_2_EC2%20to%20RDS_%20MySQL%20Database%20Flow.png)


