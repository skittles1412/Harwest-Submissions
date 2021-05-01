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

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

struct DS: set<pair<long, int>> {
	int n, ind;
	long sum, lazy;
	pair<long, int> cur;

	explicit DS(int n): n(n), ind(0), sum(0), lazy(0) {}

	void add(long x) {
		x -= lazy;
		pair<long, int> p(x, ind++);
		insert(p);
		if(size() < n) {
			sum += p.first;
			if(size() == n) {
				cur = *(--end());
			}
		}else if(p < cur) {
			sum += p.first;
			sum -= cur.first;
			cur = *(--find(cur));
		}
	}

	void radd(long x) {
		lazy += x;
	}

	long get() {
		if(size() < n) {
			return LLONG_MAX;
		}
		return sum + lazy * n;
	}
};

void solve() {
	int n, k;
	long b, c;
	cin >> n >> k >> b >> c;
	b = min(b, 5 * c);
	long arr[n];
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
		arr[i] += 1e9;
	}

	sort(arr, arr + n);
	for(int i = 0; i < n; i++) {
		dbg(arr[i]);
	}
	long ans = LLONG_MAX;
	for(int i = 0; i < 5; i++) {
		DS ds(k);
		long prev = -1;

		for(int j = 0; j < n; j++) {
			long next = ((arr[j] / 5) * 5) + i;
			if(next < arr[j]) {
				next += 5;
			}

			long nprev = next / 5;

			ds.radd((nprev - prev) * b);
			ds.add((next - arr[j]) * c);

			ans = min(ans, ds.get());
			prev = nprev;
		}
	}
	cout << ans << endl;
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
