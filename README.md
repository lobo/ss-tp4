![...](resources/image/readme-header.png)

# Time-Driven Simulation

## Build

To build the project, it is necessary to have _Maven +3.5.0_, and
_Java SE 8 Release_ installed. Then, run:

```
$ mvn clean package
```

This will generate a _\*.jar_ in the `target` folder. If you find any issues
with the building, remove the _\*.jar_ files from the _Maven_ local
repository with:

```
$ rm -fr ~/.m2/repository/ar/edu/itba/ss/*
```

Or do it manually, if you prefer.

## Execute

In the root folder (after build):

```
$ java -jar target/tp4-1.0-SNAPSHOT.jar <arguments>
```

## Help

```
$ java -jar target/tp4-1.0-SNAPSHOT.jar help
```

## Input Files Format

Receives a JSON file with the following format (see example below):

```json
{
	"fps"			: "60.0",
	"deltat"			: "20000",
	"integrator"		: "60.0",
	"system"			: "HarmonicOscillator",
	"outputfile"		: "output.txt",
	"maxtime"		: "123123",
}
```

## Output File Format

### Simulation file

```
<N>
<t0>
<x> <y> <r> <vx> <vy>
...
```

### Animated file

```
<N>
<t0>
<x> <y> <r> <v>
...
```

Where `<v>` is the module of the speed.

## Videos

Here are some videos of the simulations:

...

## Developers

This project has been built, designed and maintained by the following authors:

* [Daniel Lobo](https://github.com/lobo)
* [Agustín Golmar](https://github.com/agustin-golmar)

## Bibliography

__"Title."__ Authors. _Where & When._
