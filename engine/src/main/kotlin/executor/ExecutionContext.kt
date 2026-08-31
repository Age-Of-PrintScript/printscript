package executor

import interpreter.RuntimeEnvironment

class ExecutionContext internal constructor(
    internal val environment: RuntimeEnvironment = RuntimeEnvironment(emptyMap()),
) {
    constructor() : this(RuntimeEnvironment(emptyMap()))
}
