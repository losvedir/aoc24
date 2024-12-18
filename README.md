# Advent of Code 2024 - LLM Shoot-out

Prompt:

> Please solve the following Advent of Code problem in idiomatic, modern Java.
> You can assume the input has been downloaded to input/day<n>.txt. Use the
> class name <Model>, in package day<n>part1.
>
> =====
>
> copy/paste from AoC
>
> =====

If it gets it right, then:

> That was correct! That unlocked Part 2. Please solve Part two now, in a new
> class <Model> in package day<n>part2. Feel free to copy/paste any relevant
> code from the first part.
>
> =====
>
> copy/paste from AoC
>
> =====

## Results

| Problem | GPT-4o                                | GPT-o1                                | GPT-o1 Pro                            | Gemini Pro 1.5                        | Gemini Flash 2.0                      | Sonnet                                | Opus                                  |
| ------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- | ------------------------------------- |
| Day 1   | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: |
| 1p2     |                                       |                                       |                                       |                                       |                                       |                                       |                                       |
| 10p1    | y                                     | y                                     | y                                     | n                                     | y                                     | y                                     | n                                     |
| 10p2    | n                                     | y                                     | y                                     |                                       | y                                     | y                                     |                                       |
| 11p1    | n                                     | y                                     | y                                     | y                                     |                                       | y                                     | y                                     |
| 11p2    |                                       | y                                     | y                                     | y                                     |                                       | n                                     | n                                     |
| Day 12  |                                       |                                       | :white_check_mark: :x:                | :white_check_mark: :x:                | :x:                                   | :white_check_mark: :x:                | :x:                                   |
| Day 13  | :white_check_mark: :x:                | :white_check_mark: :white_check_mark: | :white_check_mark: :white_check_mark: | :x:                                   | :x:                                   | :white_check_mark: :white_check_mark: | :white_check_mark: :x:                |
| Day 14  | :white_check_mark:                    |                                       | :white_check_mark: :x:                | :x:                                   | :white_check_mark: :x:                | :white_check_mark: :x:                | :x:                                   |
| Day 15  | :x:                                   | :white_check_mark: :x:                | :white_check_mark: :x:                | :x:                                   | :x:                                   | :x:                                   | :x:                                   |

# Day 1

- Opus compilation failed because it failed to import `IntStream`, but that was
  a trivial fix (Click "Quick Fix" in VSCode), so I didn't count it against it

# Day 12

- None could get part 2. Only me! But the first attempt, I got the same wrong
  answer as GPT-o1 Pro. But the instructions called out

> Be especially careful when counting the fence around regions like the one full
> of type A plants; in particular, each section of fence has an in-side and an
> out-side, so the fence does not connect across the middle of the region (where
> the two B regions touch diagonally)

and eventually I figured it out.

# Day 13

Brute force works for Part 1, but Part 2 requires an algebraic solution. Only
GPT-o1+ and Sonnet identified the need, and were able to solve it that way.

With prompting Opus made noises like it understood the algebraic solution, but
it didn't work.

# Day 14

Part 2 is:

> During the bathroom break, someone notices that these robots seem awfully
> similar to ones built and used at the North Pole. If they're the same type of
> robots, they should have a hard-coded Easter egg: very rarely, most of the
> robots should arrange themselves into _a picture of a Christmas tree_. _What
> is the fewest number of seconds that must elapse for the robots to display the
> Easter egg?_

Sonnet had the right idea:

> I notice this prompt is quite open-ended and doesn't give us much information
> about what exactly constitutes a Christmas tree pattern. This seems like a
> creative interpretation challenge. Let's write a solution that looks for a
> triangular arrangement of robots that could represent a Christmas tree.
>
> This solution builds on part 1 but adds pattern recognition logic to detect a
> Christmas tree formation. Key aspects:

> Scans the grid for potential tree top points Checks for a triangular pattern
> below each potential top point Verifies the pattern meets criteria for a
> Christmas tree:

> Single point at top Widening pattern below Minimum height of 5 rows Trunk at
> bottom Minimum number of robots to form a recognizable shape

> The pattern detection is somewhat flexible to allow for different possible
> tree formations while still maintaining the basic Christmas tree shape
> requirements. The solution searches through timestamps until it finds a valid
> pattern or reaches the maximum time limit.

Unfortunately, the code didn't work.

Gemini Flash totally misinterpreted it, and updated the code to look for cycle
detection.

# Day 15

Gemini Flash cut off mid- method definition. Maybe too small of context window?
