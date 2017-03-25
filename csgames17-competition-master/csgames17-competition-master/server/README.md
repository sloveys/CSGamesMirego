# CS Games 2017 - Mobile - Server

This web server can be used to fetch and store data from the mobile application. It is built in **JavaScript** and uses an embedded datastore.

## Prerequisites

This project requires **Node.js**. If you don't have it, you can simply visit [https://nodejs.org/](https://nodejs.org/), download the latest version, and follow the setup instructions.

Once installed, you can validate its presence by running these commands:

```
node --version
npm --version
```

If one of them doesn't work, you may simply download the binaries from the [Node.js downloads page](https://nodejs.org/en/download/) and put them in this directory.

## Getting started

The server has been built to be very easy to use. First, install the dependencies:

```
npm install
```

And that's it, you're done.

You can start the server by running:

```
npm start
```

The server should be accessible at [http://localhost:3000/]([http://localhost:3000/]).

> **Pro Tip:** As it uses [`nodemon`](https://nodemon.io/) under the hood, the server should restart automatically when you modify any code file.

## How it works

The server works as a simple REST API. It should be fairly straightforward, but if you want to know more about it, you can read the **[API Documentation](https://github.com/mirego/csgames17-competition/blob/master/server/DOCUMENTATION.md)**.

There is no authentication or any data validation whatsoever, but this is enough to support the sample mobile applications.

Depending on your solution, you may want to expand it and add new features, but you may also leave it **as is** and focus on the mobile application.

### Database

To simplify development and deployment, the project uses [NeDB](https://github.com/louischatriot/nedb), an embedded datastore that works automatically, without any binary or prerequisite whatsoever. It is built using a subset of the MongoDB API, so it should be very easy to use.

Each collection of documents is represented in its own instance of NeDB, and is persisted using a simple file that contains a list of JSON objects.

### Web front-end

The web front-end on [http://localhost:3000/](http://localhost:3000/) simply shows the currently persisted data, to help you explore the project at the beginning. You don't need to edit anything in there, unless it can help you debug your project.

### Next steps

So, what's the room for improvement?

Here are some features you could add to this server:

- Real user registration and authentication
- Optimized data structure
- Request data security
- Request data validation
- Persisted data encryption
- Persisted data compression
- Image messaging
- Video messaging
- Socket communication for real-time messaging

The list is non-exhaustive, feel free to do anything you can think of!

## License

This competition is © 2017 [Mirego](http://www.mirego.com) and may be freely
distributed under the [New BSD license](http://opensource.org/licenses/BSD-3-Clause).
See the [`LICENSE.md`](https://github.com/mirego/csgames17-competition/blob/master/LICENSE.md) file.

## About Mirego

[Mirego](http://mirego.com) is a team of passionate people who believe that work is a place where you can innovate and have fun. We're a team of [talented people](http://life.mirego.com) who imagine and build beautiful Web and mobile applications. We come together to share ideas and [change the world](http://mirego.org).

We also [love open-source software](http://open.mirego.com) and we try to give back to the community as much as we can.