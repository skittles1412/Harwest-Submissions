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

const int maxn = 1e5, tmaxn = 1e7, maxrn = 1e9 + 5;

struct PST {
	struct Node {
		int lc, rc, v;

		Node(): lc(0), rc(0), v(0) {}
	} heap[tmaxn];
	int hind = 1;

	void update(int &o, int l, int r, int ind, int x, bool add) {
		if(o == 0) {
			o = hind++;
		}else {
			heap[hind++] = heap[o];
			o = hind - 1;
		}
		if(l == r) {
			if(add) {
				heap[o].v += x;
			}else {
				heap[o].v = x;
			}
		}else {
			int mid = (l + r) / 2, &lc = heap[o].lc, &rc = heap[o].rc;
			if(ind <= mid) {
				update(lc, l, mid, ind, x, add);
			}else {
				update(rc, mid + 1, r, ind, x, add);
			}
			heap[o].v = heap[lc].v + heap[rc].v;
		}
	}

	int query(int o, int l, int r, int ql, int qr) {
		if(o == 0) {
			return 0;
		}else if(ql <= l && r <= qr) {
			return heap[o].v;
		}
		int ans = 0, mid = (l + r) / 2;
		if(ql <= mid) {
			ans += query(heap[o].lc, l, mid, ql, qr);
		}
		if(mid < qr) {
			ans += query(heap[o].rc, mid + 1, r, ql, qr);
		}
		return ans;
	}

	int roots[maxn];

	void update(int ver, int ind, int x, bool add) {
		dbg("UPDATE", ver, ind, x);
		update(roots[ver], 0, maxrn, ind, x, add);
	}

	void rollback(int newver, int oldver) {
		dbg("ROLLBACK", newver, oldver);
		roots[newver] = roots[oldver];
	}

	int query(int ver, int l, int r) {
		return query(roots[ver], 0, maxrn, l, r);
	}
} st, arr;

void solve() {
	int q, cver = 1, ind = 1;
	cin >> q;
	map<string, int> m;
	for(int _ = 1; _ <= q; _++) {
		dbg(_);
		st.rollback(cver, cver - 1);
		arr.rollback(cver, cver - 1);
		string t;
		cin >> t;
		if(t == "set") {
			string s;
			int x;
			cin >> s >> x;
			int &sind = m[s];
			dbg(sind);
			if(sind == 0) {
				sind = ind++;
			}
			dbg(m[s]);
			int cv = arr.query(cver, sind, sind);
			if(cv) {
				st.update(cver, cv, -1, true);
			}
			st.update(cver, x, +1, true);
			arr.update(cver, sind, x, false);
		}else if(t == "remove") {
			string s;
			cin >> s;
			auto it = m.find(s);
			if(it != m.end()) {
				int cv = arr.query(cver, it->second, it->second);
				if(cv) {
					st.update(cver, cv, -1, true);
					arr.update(cver, it->second, 0, false);
				}
			}
		}else if(t == "query") {
			string s;
			cin >> s;
			auto it = m.find(s);
			if(it == m.end()) {
				cout << -1 << endl;
			}else {
				int cv = arr.query(cver, it->second, it->second);
				dbg(cv);
				if(cv) {
					cout << st.query(cver, 0, cv - 1) << endl;
				}else {
					cout << -1 <<endl;
				}
			}
		}else {
			int d;
			cin >> d;
			st.rollback(cver, cver - d - 1);
			arr.rollback(cver, cver - d - 1);
		}
		for(int i = 0; i < 5; i++) {
			dbg(i, st.query(cver, i, i), arr.query(cver, i, i));
		}
		cver++;
	}
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
//	arr.rollback(1, 0);
//	arr.update(1, 0, 2);
//	arr.rollback(2, 1);
//	arr.update(2, 0, 0);
//	for(int i = 0; i < 5; i++) {
//		dbg(i, arr.query(2, i, i));
//	}
	int t = 1;
//	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		dbg(_);
//		cout << "Case #" << _ << ": ";
		solve();
	}
}
