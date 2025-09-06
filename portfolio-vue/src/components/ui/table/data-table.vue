<script setup>
import { computed, toRef, ref, watch } from 'vue'
import {
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useVueTable,
  flexRender,
} from '@tanstack/vue-table'

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from './'

const props = defineProps({
  columns: {
    type: Array,
    required: true,
  },
  data: {
    type: Array,
    required: true,
  },
  filtering: {
    type: Boolean,
    default: false,
  },
  pagination: {
    type: Boolean,
    default: false,
  },
})

const columnsProp = toRef(props, 'columns')
const dataProp = toRef(props, 'data')

const sorting = ref([])
const columnFilters = ref([])
const globalFilter = ref('')

const table = useVueTable({
  get state() {
    return {
      sorting: sorting.value,
      columnFilters: columnFilters.value,
      globalFilter: globalFilter.value,
    }
  },
  onSortingChange: (updaterOrValue) => {
    sorting.value = typeof updaterOrValue === 'function'
      ? updaterOrValue(sorting.value)
      : updaterOrValue
  },
  onColumnFiltersChange: (updaterOrValue) => {
    columnFilters.value = typeof updaterOrValue === 'function'
      ? updaterOrValue(columnFilters.value)
      : updaterOrValue
  },
  onGlobalFilterChange: (updaterOrValue) => {
    globalFilter.value = typeof updaterOrValue === 'function'
      ? updaterOrValue(globalFilter.value)
      : updaterOrValue
  },
  get data() {
    return dataProp.value
  },
  get columns() {
    return columnsProp.value
  },
  getCoreRowModel: getCoreRowModel(),
  getSortedRowModel: getSortedRowModel(),
  getFilteredRowModel: getFilteredRowModel(),
  getPaginationRowModel: getPaginationRowModel(),
  debugTable: true,
})

defineExpose({ table })
</script>

<template>
  <div>
    <Table>
      <TableHead>
        <TableRow v-for="headerGroup in table.getHeaderGroups()" :key="headerGroup.id">
          <TableHeader
            v-for="header in headerGroup.headers"
            :key="header.id"
            :style="{ width: header.getSize() !== 150 ? `${header.getSize()}px` : 'auto' }"
            :class="header.column.getCanSort() ? 'cursor-pointer select-none' : ''"
            @click="header.column.getToggleSortingHandler()"
          >
            <div class="flex items-center space-x-2">
              <span>
                {{
                  flexRender(
                    header.column.columnDef.header,
                    header.getContext()
                  )
                }}
              </span>
              <span class="ml-2 flex-none">
                <template v-if="header.column.getIsSorted() === 'desc'">
                  <span class="sr-only">Sorted descending</span>
                  ▼
                </template>
                <template v-else-if="header.column.getIsSorted() === 'asc'">
                  <span class="sr-only">Sorted ascending</span>
                  ▲
                </template>
                <template v-else>
                  <span class="invisible ml-2">▼</span>
                </template>
              </span>
            </div>
          </TableHeader>
        </TableRow>
      </TableHead>
      <TableBody>
        <template v-if="table.getRowModel().rows.length">
          <TableRow
            v-for="row in table.getRowModel().rows"
            :key="row.id"
            :data-state="row.getIsSelected() ? 'selected' : undefined"
          >
            <TableCell v-for="cell in row.getVisibleCells()" :key="cell.id">
              {{ flexRender(cell.column.columnDef.cell, cell.getContext()) }}
            </TableCell>
          </TableRow>
        </template>
        <template v-else>
          <TableRow>
            <TableCell :colspan="columnsProp.length" class="h-24 text-center">
              No results.
            </TableCell>
          </TableRow>
        </template>
      </TableBody>
    </Table>
  </div>
</template>

