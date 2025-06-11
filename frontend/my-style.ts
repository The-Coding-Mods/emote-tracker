import type { CustomThemeConfig } from "@skeletonlabs/tw-plugin";

export const myStyle: CustomThemeConfig = {
	name: "my-style",
	properties: {
		// =~= Theme Properties =~=
		"--theme-font-family-base": "system-ui",
		"--theme-font-family-heading": "system-ui",
		"--theme-font-color-base": "0 0 0",
		"--theme-font-color-dark": "255 255 255",
		"--theme-rounded-base": "6px",
		"--theme-rounded-container": "4px",
		"--theme-border-base": "2px",
		// =~= Theme On-X Colors =~=
		"--on-primary": "0 0 0",
		"--on-secondary": "0 0 0",
		"--on-tertiary": "0 0 0",
		"--on-success": "255 255 255",
		"--on-warning": "0 0 0",
		"--on-error": "255 255 255",
		"--on-surface": "255 255 255",
		// =~= Theme Colors  =~=
		// primary | #65a30d
		"--color-primary-50": "232 241 219", // #e8f1db
		"--color-primary-100": "224 237 207", // #e0edcf
		"--color-primary-200": "217 232 195", // #d9e8c3
		"--color-primary-300": "193 218 158", // #c1da9e
		"--color-primary-400": "147 191 86", // #93bf56
		"--color-primary-500": "101 163 13", // #65a30d
		"--color-primary-600": "91 147 12", // #5b930c
		"--color-primary-700": "76 122 10", // #4c7a0a
		"--color-primary-800": "61 98 8", // #3d6208
		"--color-primary-900": "49 80 6", // #315006
		// secondary | #9ced2c
		"--color-secondary-50": "240 252 223", // #f0fcdf
		"--color-secondary-100": "235 251 213", // #ebfbd5
		"--color-secondary-200": "230 251 202", // #e6fbca
		"--color-secondary-300": "215 248 171", // #d7f8ab
		"--color-secondary-400": "186 242 107", // #baf26b
		"--color-secondary-500": "156 237 44", // #9ced2c
		"--color-secondary-600": "140 213 40", // #8cd528
		"--color-secondary-700": "117 178 33", // #75b221
		"--color-secondary-800": "94 142 26", // #5e8e1a
		"--color-secondary-900": "76 116 22", // #4c7416
		// tertiary | #b4e1fe
		"--color-tertiary-50": "244 251 255", // #f4fbff
		"--color-tertiary-100": "240 249 255", // #f0f9ff
		"--color-tertiary-200": "236 248 255", // #ecf8ff
		"--color-tertiary-300": "225 243 255", // #e1f3ff
		"--color-tertiary-400": "203 234 254", // #cbeafe
		"--color-tertiary-500": "180 225 254", // #b4e1fe
		"--color-tertiary-600": "162 203 229", // #a2cbe5
		"--color-tertiary-700": "135 169 191", // #87a9bf
		"--color-tertiary-800": "108 135 152", // #6c8798
		"--color-tertiary-900": "88 110 124", // #586e7c
		// success | #1d67fc
		"--color-success-50": "221 232 255", // #dde8ff
		"--color-success-100": "210 225 254", // #d2e1fe
		"--color-success-200": "199 217 254", // #c7d9fe
		"--color-success-300": "165 194 254", // #a5c2fe
		"--color-success-400": "97 149 253", // #6195fd
		"--color-success-500": "29 103 252", // #1d67fc
		"--color-success-600": "26 93 227", // #1a5de3
		"--color-success-700": "22 77 189", // #164dbd
		"--color-success-800": "17 62 151", // #113e97
		"--color-success-900": "14 50 123", // #0e327b
		// warning | #af7818
		"--color-warning-50": "243 235 220", // #f3ebdc
		"--color-warning-100": "239 228 209", // #efe4d1
		"--color-warning-200": "235 221 197", // #ebddc5
		"--color-warning-300": "223 201 163", // #dfc9a3
		"--color-warning-400": "199 161 93", // #c7a15d
		"--color-warning-500": "175 120 24", // #af7818
		"--color-warning-600": "158 108 22", // #9e6c16
		"--color-warning-700": "131 90 18", // #835a12
		"--color-warning-800": "105 72 14", // #69480e
		"--color-warning-900": "86 59 12", // #563b0c
		// error | #a21111
		"--color-error-50": "241 219 219", // #f1dbdb
		"--color-error-100": "236 207 207", // #eccfcf
		"--color-error-200": "232 196 196", // #e8c4c4
		"--color-error-300": "218 160 160", // #daa0a0
		"--color-error-400": "190 88 88", // #be5858
		"--color-error-500": "162 17 17", // #a21111
		"--color-error-600": "146 15 15", // #920f0f
		"--color-error-700": "122 13 13", // #7a0d0d
		"--color-error-800": "97 10 10", // #610a0a
		"--color-error-900": "79 8 8", // #4f0808
		// surface | #18181b
		"--color-surface-50": "220 220 221", // #dcdcdd
		"--color-surface-100": "209 209 209", // #d1d1d1
		"--color-surface-200": "197 197 198", // #c5c5c6
		"--color-surface-300": "163 163 164", // #a3a3a4
		"--color-surface-400": "93 93 95", // #5d5d5f
		"--color-surface-500": "24 24 27", // #18181b
		"--color-surface-600": "22 22 24", // #161618
		"--color-surface-700": "18 18 20", // #121214
		"--color-surface-800": "14 14 16", // #0e0e10
		"--color-surface-900": "12 12 13", // #0c0c0d
	},
};
