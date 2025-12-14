package com.github.vmaerten.filenesting.patterns

/**
 * Default file nesting patterns, ported from antfu/vscode-file-nesting-config.
 *
 * These patterns define which files should be nested under which parent files
 * in the Project View. Patterns support wildcards (*) for flexible matching.
 *
 * @see <a href="https://github.com/antfu/vscode-file-nesting-config">vscode-file-nesting-config</a>
 */
object DefaultPatterns {

    /**
     * All default nesting patterns.
     */
    val patterns: List<NestingPattern> by lazy {
        listOf(
            // === Package Managers & Build Tools ===
            packageJson(),
            cargoToml(),
            goMod(),
            composerJson(),
            gemfile(),
            pubspecYaml(),
            pyprojectToml(),
            requirementsTxt(),
            mixExs(),

            // === Configuration Files ===
            gitignore(),
            dockerfile(),
            env(),
            tsconfigJson(),
            denoJson(),
            makefilePattern(),
            cmakeLists(),
            buildBazel(),
            flakeNix(),
            ansibleCfg(),

            // === Documentation ===
            readme(),

            // === Source Files ===
            typescript(),
            javascript(),
            typescriptReact(),
            javascriptReact(),
            vue(),
            svelte(),
            angularComponent(),
            css(),
            cSource(),
            cppSource(),
            goSource(),
            dartSource(),
            pythonSource(),
            javaSource(),
            csharpSource(),

            // === Framework Configs ===
            viteConfig(),
            nextConfig(),
            nuxtConfig(),
            astroConfig(),
            svelteConfig(),
            remixConfig(),
            gatsbyConfig(),

            // === SvelteKit Routing ===
            sveltekitPage(),
            sveltekitLayout(),

            // === Other ===
            texSource(),
            agentsMd(),
            sanityConfig(),
            tauriConfig(),
        )
    }

    // === Package Managers & Build Tools ===

    private fun packageJson() = NestingPattern(
        parent = "package.json",
        children = listOf(
            // Lock files
            "package-lock.json", "pnpm-lock.yaml", "yarn.lock", "bun.lockb", "bun.lock",
            "npm-shrinkwrap.json", ".npmrc", ".yarnrc", ".yarnrc.yml", "pnpm-workspace.yaml",
            ".pnpmfile.cjs", ".npmignore",

            // TypeScript
            "tsconfig.json", "tsconfig.*.json", "tsconfig-*.json",
            "jsconfig.json", "jsconfig.*.json",

            // ESLint
            ".eslintrc", ".eslintrc.*", "eslint.config.*", ".eslintignore", ".eslintcache",

            // Prettier
            ".prettierrc", ".prettierrc.*", "prettier.config.*", ".prettierignore",

            // Stylelint
            ".stylelintrc", ".stylelintrc.*", "stylelint.config.*", ".stylelintignore", ".stylelintcache",

            // Build tools
            "vite.config.*", "vitest.config.*", "vitest.workspace.*",
            "webpack.config.*", "webpack.*.config.*",
            "rollup.config.*", "esbuild.config.*", "esbuild.mjs",
            "tsup.config.*", "unbuild.config.*", "build.config.*",
            "turbo.json", "lerna.json", "nx.json", "project.json", "rush.json",

            // Testing
            "jest.config.*", "jest.setup.*", "jest-preset.*",
            "playwright.config.*", "playwright-ct.config.*",
            "cypress.config.*", "cypress.json",
            ".mocharc.*", "mocha.opts",
            "karma.conf.*", "protractor.conf.*",
            "nightwatch.conf.*", "wdio.conf.*",

            // Other configs
            ".babelrc", ".babelrc.*", "babel.config.*",
            "postcss.config.*", ".postcssrc", ".postcssrc.*",
            "tailwind.config.*",
            ".browserslistrc", "browserslist",
            ".editorconfig",
            ".commitlintrc", ".commitlintrc.*", "commitlint.config.*",
            ".cz-config.js", ".czrc",
            ".huskyrc", ".huskyrc.*", ".husky",
            ".lintstagedrc", ".lintstagedrc.*", "lint-staged.config.*",
            ".ls-lint.yml",
            ".markdownlint.*", ".markdownlintignore",
            ".node-version", ".nvmrc", ".tool-versions",
            ".nodemon.json", "nodemon.json",
            ".env", ".env.*", ".envrc",
            ".gitpod.yml",
            ".releaserc", ".releaserc.*", "release.config.*", ".changeset",
            ".sentryclirc",
            ".swcrc",
            ".vercelignore", "vercel.json",
            ".nowignore", "now.json",
            "netlify.toml",
            "firebase.json", ".firebaserc",
            "renovate.json", "renovate.json5", ".renovaterc", ".renovaterc.json",
            "simple-git-hooks.cjs", ".simple-git-hooks.*",
            "sonar-project.properties",
            "lefthook.yml", "lefthook.yaml", ".lefthook.yml", ".lefthook.yaml",
            ".cspell.json", "cspell.json", "cspell.config.*",
            ".knip.json", "knip.json", "knip.config.*", "knip.ts",
            "biome.json", "biome.jsonc",
            "dprint.json", ".dprint.json", "dprint.jsonc", ".dprint.jsonc",
            ".watchmanconfig",
        )
    )

    private fun cargoToml() = NestingPattern(
        parent = "Cargo.toml",
        children = listOf(
            "Cargo.lock", "Cargo.Bazel.lock",
            ".rustfmt.toml", "rustfmt.toml",
            ".clippy.toml", "clippy.toml",
            "rust-toolchain.toml",
            "cross.toml", "insta.yaml",
        )
    )

    private fun goMod() = NestingPattern(
        parent = "go.mod",
        children = listOf("go.sum")
    )

    private fun composerJson() = NestingPattern(
        parent = "composer.json",
        children = listOf(
            "composer.lock",
            "phpunit.xml", "phpunit.xml.*",
            "psalm.xml", "psalm*.xml",
            ".php*.cache", ".phpunit.*",
            "phpstan.neon", "phpstan.neon.*",
        )
    )

    private fun gemfile() = NestingPattern(
        parent = "Gemfile",
        children = listOf("Gemfile.lock", ".ruby-version", ".ruby-gemset")
    )

    private fun pubspecYaml() = NestingPattern(
        parent = "pubspec.yaml",
        children = listOf(
            "pubspec.lock", "pubspec_overrides.yaml",
            ".metadata", ".packages",
            "analysis_options.yaml", "all_lint_rules.yaml",
            "build.yaml",
        )
    )

    private fun pyprojectToml() = NestingPattern(
        parent = "pyproject.toml",
        children = listOf(
            "poetry.lock", "poetry.toml",
            "pdm.lock", ".pdm.toml", ".pdm-python",
            "uv.lock",
            "setup.py", "setup.cfg",
            "Pipfile", "Pipfile.lock",
            "hatch.toml",
            "requirements.txt", "requirements*.txt",
            "requirements.in", "requirements*.in",
            ".python-version",
            "pytest.ini", "conftest.py",
            "tox.ini", "noxfile.py",
            ".flake8", ".pep8", ".pylintrc", "pylintrc",
            ".isort.cfg", "pyproject.toml",
            "mypy.ini", ".mypy.ini",
            ".coveragerc", ".coverage",
            "MANIFEST.in",
        )
    )

    private fun requirementsTxt() = NestingPattern(
        parent = "requirements.txt",
        children = listOf(
            "requirements*.txt", "requirements*.in", "requirements*.pip",
            "constraints.txt", "constraints*.txt",
        )
    )

    private fun mixExs() = NestingPattern(
        parent = "mix.exs",
        children = listOf(
            "mix.lock",
            ".formatter.exs", ".credo.exs", ".dialyzer_ignore.exs",
            ".iex.exs", ".tool-versions",
        )
    )

    // === Configuration Files ===

    private fun gitignore() = NestingPattern(
        parent = ".gitignore",
        children = listOf(
            ".gitattributes", ".gitmodules", ".gitmessage",
            ".lfsconfig", ".mailmap", ".git-blame*",
        )
    )

    private fun dockerfile() = NestingPattern(
        parent = "Dockerfile",
        children = listOf(
            "Dockerfile.*", "dockerfile.*", "*.dockerfile",
            ".dockerignore",
            "docker-compose.yml", "docker-compose.yaml", "docker-compose.*.yml", "docker-compose.*.yaml",
            "compose.yml", "compose.yaml", "compose.*.yml", "compose.*.yaml",
            ".devcontainer.json", "devcontainer.json",
            "captain-definition",
        )
    )

    private fun env() = NestingPattern(
        parent = ".env",
        children = listOf(
            ".env.*", "*.env",
            ".envrc",
            "env.d.ts",
        )
    )

    private fun tsconfigJson() = NestingPattern(
        parent = "tsconfig.json",
        children = listOf(
            "tsconfig.*.json", "tsconfig-*.json",
            "*.tsbuildinfo",
        )
    )

    private fun denoJson() = NestingPattern(
        parent = "deno.json*",
        children = listOf(
            "deno.lock",
            "import_map.json", "import-map.json",
            "tsconfig.json",
            ".env", ".env.*",
        )
    )

    private fun makefilePattern() = NestingPattern(
        parent = "Makefile",
        children = listOf("*.mk", "Makefile.*")
    )

    private fun cmakeLists() = NestingPattern(
        parent = "CMakeLists.txt",
        children = listOf(
            "*.cmake", "*.cmake.in",
            ".cmake-format.yaml", "CMakePresets.json", "CMakeCache.txt",
        )
    )

    private fun buildBazel() = NestingPattern(
        parent = "BUILD.bazel",
        children = listOf(
            "*.bzl", "*.bazel", "*.bazelrc",
            "bazel.rc", ".bazelignore", ".bazelproject", ".bazelversion",
            "MODULE.bazel.lock", "WORKSPACE", "WORKSPACE.bazel",
        )
    )

    private fun flakeNix() = NestingPattern(
        parent = "flake.nix",
        children = listOf("flake.lock", "default.nix", "shell.nix")
    )

    private fun ansibleCfg() = NestingPattern(
        parent = "ansible.cfg",
        children = listOf(".ansible-lint", "requirements.yml")
    )

    // === Documentation ===

    private fun readme() = NestingPattern(
        parent = "README*",
        children = listOf(
            "readme*",
            "AUTHORS*", "authors*",
            "CHANGELOG*", "changelog*", "HISTORY*", "history*", "CHANGES*", "changes*", "RELEASE*", "release*",
            "CONTRIBUTING*", "contributing*",
            "CONTRIBUTORS*", "contributors*",
            "CODE_OF_CONDUCT*", "code_of_conduct*",
            "LICENSE*", "license*", "LICENCE*", "licence*", "COPYING*", "copying*",
            "SECURITY*", "security*",
            "SUPPORT*", "support*",
            "CODEOWNERS", ".codeowners",
            "FUNDING*", ".github",
            "SPONSORS*", "sponsors*",
            "BACKERS*", "backers*",
        )
    )

    // === Source Files ===

    private fun typescript() = NestingPattern(
        parent = "*.ts",
        children = listOf(
            "\$(capture).js", "\$(capture).d.ts", "\$(capture).d.ts.map",
            "\$(capture).js.map",
            "\$(capture).*.ts", "\$(capture)_*.ts",
            "\$(capture)_*.js", "\$(capture).*.js",
        )
    )

    private fun javascript() = NestingPattern(
        parent = "*.js",
        children = listOf(
            "\$(capture).js.map",
            "\$(capture).d.ts", "\$(capture).d.ts.map",
            "\$(capture).*.js", "\$(capture)_*.js",
            "\$(capture).js.flow",
        )
    )

    private fun typescriptReact() = NestingPattern(
        parent = "*.tsx",
        children = listOf(
            "\$(capture).ts",
            "\$(capture).*.tsx", "\$(capture)_*.tsx",
            "\$(capture)_*.ts", "\$(capture).*.ts",
            "\$(capture).css", "\$(capture).module.css",
            "\$(capture).scss", "\$(capture).module.scss", "\$(capture).module.scss.d.ts",
            "\$(capture).less", "\$(capture).module.less", "\$(capture).module.less.d.ts",
            "\$(capture).css.ts",
        )
    )

    private fun javascriptReact() = NestingPattern(
        parent = "*.jsx",
        children = listOf(
            "\$(capture).js",
            "\$(capture).*.jsx", "\$(capture)_*.jsx",
            "\$(capture)_*.js", "\$(capture).*.js",
            "\$(capture).css", "\$(capture).module.css",
            "\$(capture).scss", "\$(capture).module.scss", "\$(capture).module.scss.d.ts",
            "\$(capture).less", "\$(capture).module.less", "\$(capture).module.less.d.ts",
        )
    )

    private fun vue() = NestingPattern(
        parent = "*.vue",
        children = listOf(
            "\$(capture).*.ts", "\$(capture).*.js",
            "\$(capture).story.vue",
        )
    )

    private fun svelte() = NestingPattern(
        parent = "*.svelte",
        children = listOf(
            "\$(capture).*.ts", "\$(capture).*.js",
            "\$(capture).svelte.ts", "\$(capture).svelte.js",
        )
    )

    private fun angularComponent() = NestingPattern(
        parent = "*.component.ts",
        children = listOf(
            "\$(capture).component.html",
            "\$(capture).component.spec.ts",
            "\$(capture).component.css", "\$(capture).component.scss",
            "\$(capture).component.sass", "\$(capture).component.less",
        )
    )

    private fun css() = NestingPattern(
        parent = "*.css",
        children = listOf("\$(capture).css.map", "\$(capture).*.css")
    )

    private fun cSource() = NestingPattern(
        parent = "*.c",
        children = listOf("\$(capture).h")
    )

    private fun cppSource() = NestingPattern(
        parent = "*.cpp",
        children = listOf("\$(capture).hpp", "\$(capture).h", "\$(capture).hxx", "\$(capture).hh")
    )

    private fun goSource() = NestingPattern(
        parent = "*.go",
        children = listOf("\$(capture)_test.go")
    )

    private fun dartSource() = NestingPattern(
        parent = "*.dart",
        children = listOf(
            "\$(capture).freezed.dart", "\$(capture).g.dart", "\$(capture).mapper.dart",
        )
    )

    private fun pythonSource() = NestingPattern(
        parent = "*.py",
        children = listOf("\$(capture).pyi")
    )

    private fun javaSource() = NestingPattern(
        parent = "*.java",
        children = listOf("\$(capture).class")
    )

    private fun csharpSource() = NestingPattern(
        parent = "*.cs",
        children = listOf("\$(capture).*.cs", "\$(capture).cs.uid")
    )

    // === Framework Configs ===

    private fun viteConfig() = NestingPattern(
        parent = "vite.config.*",
        children = libraryPatterns()
    )

    private fun nextConfig() = NestingPattern(
        parent = "next.config.*",
        children = listOf("next-env.d.ts", "next-i18next.config.*") + libraryPatterns()
    )

    private fun nuxtConfig() = NestingPattern(
        parent = "nuxt.config.*",
        children = listOf(".nuxtignore", ".nuxtrc") + libraryPatterns()
    )

    private fun astroConfig() = NestingPattern(
        parent = "astro.config.*",
        children = libraryPatterns()
    )

    private fun svelteConfig() = NestingPattern(
        parent = "svelte.config.*",
        children = listOf("mdsvex.config.js", "vite.config.*", "houdini.config.*") + libraryPatterns()
    )

    private fun remixConfig() = NestingPattern(
        parent = "remix.config.*",
        children = listOf("remix.*") + libraryPatterns()
    )

    private fun gatsbyConfig() = NestingPattern(
        parent = "gatsby-config.*",
        children = listOf(
            "gatsby-browser.*", "gatsby-node.*", "gatsby-ssr.*", "gatsby-transformer.*",
        ) + libraryPatterns()
    )

    // === SvelteKit Routing ===

    private fun sveltekitPage() = NestingPattern(
        parent = "+page.svelte",
        children = listOf(
            "+page.server.ts", "+page.server.js",
            "+page.ts", "+page.js",
            "+page.gql",
        )
    )

    private fun sveltekitLayout() = NestingPattern(
        parent = "+layout.svelte",
        children = listOf(
            "+layout.server.ts", "+layout.server.js",
            "+layout.ts", "+layout.js",
            "+layout.gql",
        )
    )

    // === Other ===

    private fun texSource() = NestingPattern(
        parent = "*.tex",
        children = listOf(
            "\$(capture).acn", "\$(capture).acr", "\$(capture).alg",
            "\$(capture).aux", "\$(capture).bbl", "\$(capture).blg",
            "\$(capture).fdb_latexmk", "\$(capture).fls", "\$(capture).glg",
            "\$(capture).glo", "\$(capture).gls", "\$(capture).idx",
            "\$(capture).ind", "\$(capture).lof", "\$(capture).log",
            "\$(capture).lot", "\$(capture).out", "\$(capture).pdf",
            "\$(capture).synctex.gz", "\$(capture).toc", "\$(capture).xdv",
        )
    )

    private fun agentsMd() = NestingPattern(
        parent = "AGENTS.md",
        children = listOf(
            "AGENT.md", "CLAUDE.md", "CLAUDE.local.md", "GEMINI.md",
            ".clinerules", ".cursorrules", ".replit.md", ".windsurfrules",
        )
    )

    private fun sanityConfig() = NestingPattern(
        parent = "sanity.config.*",
        children = listOf("sanity.cli.*", "sanity.types.ts", "schema.json")
    )

    private fun tauriConfig() = NestingPattern(
        parent = "tauri.conf.json",
        children = listOf("tauri.*.conf.json")
    )

    // === Helper Functions ===

    /**
     * Common library/tool configuration patterns that appear under framework configs.
     */
    private fun libraryPatterns(): List<String> = listOf(
        // TypeScript
        "tsconfig.json", "tsconfig.*.json",
        "jsconfig.json", "jsconfig.*.json",

        // Linting
        ".eslintrc", ".eslintrc.*", "eslint.config.*", ".eslintignore",
        ".prettierrc", ".prettierrc.*", "prettier.config.*", ".prettierignore",
        ".stylelintrc", ".stylelintrc.*", "stylelint.config.*", ".stylelintignore",

        // Build
        ".babelrc", ".babelrc.*", "babel.config.*",
        "postcss.config.*", ".postcssrc", ".postcssrc.*",
        "tailwind.config.*",

        // Testing
        "jest.config.*", "vitest.config.*",
        "playwright.config.*", "cypress.config.*",

        // Env
        ".env", ".env.*",

        // Other
        ".editorconfig", ".browserslistrc",
    )
}
