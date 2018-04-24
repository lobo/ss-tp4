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
$ java -jar target/tp4-1.0-SNAPSHOT.jar <simulate | animate>
```

## Help

```
$ java -jar target/tp4-1.0-SNAPSHOT.jar help
```

## Input Files Format

Receives a JSON file with the following format (see example below):

```json
{
	"fps"			: "100",
	"deltat"		: "100.0",
	"integrator"	: "Beeman",
	"system"		: "GravitationalField",
	"output"		: "resources/data/beeman.data",
	"maxtime"		: "157680000.0"
}
```

## Output File Format

### Simulation file

```
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

[Video of the simulation](https://youtu.be/R_q-2zFcNWA)

## Simulaciones para otras fechas

### Mar 77

* Jupiter: 
	* 3.9857e+00 AU
	*  458.6806 days
* Saturn:
	*  8.2487e+00 AU
	*  521.412 days

- - - -

### Jun 77
* Jupiter
	* 3.5735e+00 AU
	* 283.3333 days
* Saturn
	* 7.7907e+00 AU
	* 380.9028 days

- - - -

### Sept 77

* Jupiter: 
	* 1.8653e-03 AU
	* 545.8333 days
* Saturn:
	* 6.5126e-04 AU
	* 1166.088 days

- - - -

### Dec 77

* Jupiter: 
	*  3.9226e+00 AU
	*  1825 days
* Saturn:
	*  8.1816e+00 AU
	*  74.537 days

- - - -

### Mar 78

* Jupiter: 
	*  4.2744e+00 AU
	*  506.1343 days
* Saturn:
	*  8.2790e+00 AU
	*  0 days


## Developers

This project has been built, designed and maintained by the following authors:

* [Daniel Lobo](https://github.com/lobo)
* [Agustín Golmar](https://github.com/agustin-golmar)
