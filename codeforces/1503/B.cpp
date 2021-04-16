#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

#define sz(x) int((x).size())

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
	int n;
	cin >> n;
	vector<pair<int, int>> a, b;
	for(int i = 0; i < n; i++) {
		for(int j = 0; j < n; j++) {
			if((i + j) % 2) {
				a.emplace_back(i, j);
			}else {
				b.emplace_back(i, j);
			}
		}
	}
	auto print = [&] (int color, int i, int j) {
		cout << color << " " << i + 1 << " " << j + 1 << endl;
	};
	for(int _ = 0; _ < n * n; _++) {
		int x;
		cin >> x;
		if(x == 1) {
			if(b.empty()) {
				const auto &cur = a.back();
				a.pop_back();
				print(3, cur.first, cur.second);
			}else {
				const auto &cur = b.back();
				b.pop_back();
				print(2, cur.first, cur.second);
			}
		}else if(x == 2) {
			if(a.empty()) {
				const auto &cur = b.back();
				b.pop_back();
				print(3, cur.first, cur.second);
			}else {
				const auto &cur = a.back();
				a.pop_back();
				print(1, cur.first, cur.second);
			}
		}else if(x == 3) {
			if(a.empty()) {
				const auto &cur = b.back();
				b.pop_back();
				print(2, cur.first, cur.second);
			}else {
				const auto &cur = a.back();
				a.pop_back();
				print(1, cur.first, cur.second);
			}
		}
	}
}
