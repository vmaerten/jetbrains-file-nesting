# File Nesting

Advanced file nesting for JetBrains IDEs, similar to VS Code's file nesting feature.

Groups related configuration files under their parent file in the Project View.

## Installation

Install from the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/29503-file-nesting) or search for "File Nesting" in your IDE's plugin settings.

## Usage

1. Install the plugin and restart your IDE
2. Open any project — file nesting is enabled by default
3. Related files are now grouped under their parent in the Project View

To toggle: **Settings → Appearance → File Nesting**

## Supported Patterns

### Package Managers & Build Tools

| Parent | Nested Files |
|--------|--------------|
| `package.json` | lock files, tsconfig, eslint, prettier, vite, webpack, jest, babel, tailwind, and 80+ more |
| `Cargo.toml` | Cargo.lock, rustfmt.toml, clippy.toml |
| `go.mod` | go.sum |
| `composer.json` | composer.lock, phpunit.xml, phpstan.neon |
| `Gemfile` | Gemfile.lock, .ruby-version |
| `pubspec.yaml` | pubspec.lock, analysis_options.yaml |
| `pyproject.toml` | poetry.lock, requirements.txt, pytest.ini, mypy.ini |
| `mix.exs` | mix.lock, .formatter.exs, .credo.exs |

### Configuration Files

| Parent | Nested Files |
|--------|--------------|
| `.gitignore` | .gitattributes, .gitmodules, .mailmap |
| `Dockerfile` | docker-compose.yml, .dockerignore, devcontainer.json |
| `Taskfile.yml` | .taskrc.yml, .taskrc.yaml |
| `.env` | .env.*, *.env, env.d.ts |
| `tsconfig.json` | tsconfig.*.json, *.tsbuildinfo |
| `Makefile` | *.mk, Makefile.* |
| `CMakeLists.txt` | *.cmake, CMakePresets.json |
| `BUILD.bazel` | *.bzl, WORKSPACE, MODULE.bazel.lock |
| `flake.nix` | flake.lock, default.nix, shell.nix |

### Documentation

| Parent | Nested Files |
|--------|--------------|
| `README` | LICENSE, CHANGELOG, CONTRIBUTING, CODE_OF_CONDUCT, AUTHORS, SECURITY, .github |

### Source Files

| Parent | Nested Files |
|--------|--------------|
| `*.ts` | *.js, *.d.ts, *.js.map |
| `*.tsx` | *.ts, *.css, *.module.scss |
| `*.vue` | *.story.vue, related scripts |
| `*.svelte` | *.svelte.ts, *.svelte.js |
| `*.component.ts` | Angular component files (html, spec, css, scss) |
| `*.go` | *_test.go |
| `*.c` / `*.cpp` | header files (*.h, *.hpp) |
| `*.py` | *.pyi (type stubs) |
| `*.java` | *.class |
| `*.dart` | *.freezed.dart, *.g.dart |
| `*.tex` | LaTeX output files (*.aux, *.pdf, *.log, etc.) |

### Framework Configs

| Parent | Nested Files |
|--------|--------------|
| `vite.config.*` | tsconfig, eslint, prettier, tailwind, jest |
| `next.config.*` | next-env.d.ts, next-i18next.config |
| `nuxt.config.*` | .nuxtignore, .nuxtrc |
| `astro.config.*` | common library configs |
| `svelte.config.*` | mdsvex.config, vite.config, houdini.config |
| `gatsby-config.*` | gatsby-browser, gatsby-node, gatsby-ssr |

### SvelteKit Routing

| Parent | Nested Files |
|--------|--------------|
| `+page.svelte` | +page.server.ts, +page.ts, +page.gql |
| `+layout.svelte` | +layout.server.ts, +layout.ts |

## License

MIT
