#include "bits/extc++.h"

using namespace std;

template<class T, class U = less<T>>
using rt = __gnu_pbds::tree<T, __gnu_pbds::null_type, U, __gnu_pbds::rb_tree_tag, __gnu_pbds::tree_order_statistics_node_update>;

template<class T, class U>
void sep(T &out, const string &s, const U &u) {
	out << u;
}

template<class T, class Head, class ...Tail>
void sep(T &out, const string &s, const Head &h, const Tail &...t) {
	out << h << s;
	sep(out, s, t...);
}

#ifdef DEBUG
#define dbg(...)                                                      \
cerr << "L" << __LINE__ << " [" << #__VA_ARGS__ << "]" << ": ";       \
sep(cerr, " | ", __VA_ARGS__);                                        \
cerr << endl
#else
#define cerr if(false) cerr
#define dbg(...) cerr
#endif

//imagine a language where int = long
#define long int64_t

#define sz(x) int((x).size())

const int len[]{26, 25, 23};

void solve() {
	string s;
	cin >> s;
	int n = sz(s);
	vector<vector<int>> arr[3];
	for(int i = 0; i < 3; i++) {
		int cn = len[i];
		string cur;
		for(int j = 0; j < n; j++) {
			cur += 'a' + j % cn;
		}
		cout << "? " << cur << endl;
		cin >> cur;
		arr[i].resize(cn);
		for(int j = 0; j < n; j++) {
			arr[i][cur[j] - 'a'].push_back(j);
		}
	}
	char ans[n];
	auto merge = [&](const vector<int> &a, const vector<int> &b) {
		vector<int> ret;
		int i = 0, j = 0;
		while(i < sz(a) && j < sz(b)) {
			if(a[i] == b[j]) {
				ret.push_back(a[i]);
				i++;
				j++;
			}else if(a[i] < b[j]) {
				i++;
			}else {
				j++;
			}
		}
		return ret;
	};
	for(int i = 0; i < n; i++) {
		int a = i % 26, b = i % 25, c = i % 23;
		auto cur = merge(merge(arr[0][a], arr[1][b]), arr[2][c]);
		dbg(sz(cur));
		assert(sz(cur) == 1);
		ans[i] = s[cur[0]];
	}
	cout << "! " << string(ans, ans + n) << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t = 1;
//	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		dbg(_);
//		cout << "Case #" << _ << ": ";
		solve();
	}
}
