# bb4-math

[![CI](https://github.com/bb4/bb4-math/actions/workflows/ci.yml/badge.svg)](https://github.com/bb4/bb4-math/actions/workflows/ci.yml)

Core math utilities used across bb4 projects: ranges and axis cut-points, interpolation, 1D functions, combinatorics, complex numbers, linear algebra helpers, and multi-dimensional arrays.

## Using it

```groovy
implementation 'com.barrybecker4:bb4-math:2.0.0'
```

See the [releases page](https://github.com/bb4/bb4-math/releases) or [Maven Central](https://central.sonatype.com/artifact/com.barrybecker4/bb4-math) for newer versions.

## What's inside

- **`MathUtil`** — GCD/LCM, factorial, combinations/permutations, log10/exp10, and shared epsilon constants
- **`Range`** — double-precision min/max range with extent, containment, and unit-scale mapping
- **`cutpoints`** — nicely rounded tick/label positions for a numeric range (`CutPointGenerator`, tight vs. loose labeling)
- **`interpolation`** — interpolate through an array of y-values (linear, cosine, cubic, Hermite, step)
- **`function`** — 1D function types (`LinearFunction`, `LogFunction`, `ArrayFunction`, `PiecewiseFunction`, Gaussian `ErrorFunction`, wave forms, invertibility helpers)
- **`combinatorics`** — memory-efficient iterators over combinations (`Combinater`) and permutations (`Permuter`)
- **`complex`** — complex numbers and ranges with arithmetic and exponentiation
- **`linear`** — N-dimensional `Vector`, linear utilities, and a conjugate-gradient solver for `Ax = b`
- **`multiarray`** — arbitrarily dimensioned arrays of doubles with multi-index ↔ flat-index mapping
- **`random`** — skewed Gaussian random numbers (`SkewedRandom`)

## Building from source

See the [Building bb4 Projects wiki](https://github.com/bb4/bb4-common/wiki/Building-bb4-Projects).

## License

MIT — see [LICENSE](LICENSE).
